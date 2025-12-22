package org.cescfe.book_publishing_app.ui.author

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.cescfe.book_publishing_app.data.author.repository.AuthorsRepositoryImpl
import org.cescfe.book_publishing_app.data.shared.remote.RetrofitClient
import org.cescfe.book_publishing_app.domain.author.model.CreateAuthorRequest
import org.cescfe.book_publishing_app.domain.author.repository.AuthorsRepository
import org.cescfe.book_publishing_app.domain.author.validation.AuthorValidation
import org.cescfe.book_publishing_app.domain.author.validation.ValidationResult
import org.cescfe.book_publishing_app.domain.shared.DomainErrorType
import org.cescfe.book_publishing_app.domain.shared.DomainResult
import org.cescfe.book_publishing_app.ui.shared.toStringResId

data class CreateAuthorUiState(
    val fullName: String = "",
    val pseudonym: String = "",
    val biography: String = "",
    val email: String = "",
    val website: String = "",
    val isLoading: Boolean = false,
    @get:StringRes val fullNameError: Int? = null,
    @get:StringRes val pseudonymError: Int? = null,
    @get:StringRes val biographyError: Int? = null,
    @get:StringRes val emailError: Int? = null,
    @get:StringRes val websiteError: Int? = null,
    @get:StringRes val errorResId: Int? = null,
    val sessionExpired: Boolean = false,
    val createdAuthorId: String? = null
) {
    val isFormValid: Boolean
        get() = fullName.isNotBlank() &&
            fullNameError == null &&
            pseudonymError == null &&
            biographyError == null &&
            emailError == null &&
            websiteError == null
}

class CreateAuthorViewModel(
    private val authorsRepository: AuthorsRepository = AuthorsRepositoryImpl(
        RetrofitClient.authorsApi
    )
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateAuthorUiState())
    val uiState: StateFlow<CreateAuthorUiState> = _uiState.asStateFlow()

    fun onFullNameChange(fullName: String) {
        val error = AuthorValidation.validateFullName(fullName).errorResIdOrNull()
        _uiState.value = _uiState.value.copy(
            fullName = fullName,
            fullNameError = error,
            errorResId = null
        )
    }

    fun onPseudonymChange(pseudonym: String) {
        val error = AuthorValidation.validatePseudonym(pseudonym).errorResIdOrNull()
        _uiState.value = _uiState.value.copy(
            pseudonym = pseudonym,
            pseudonymError = error,
            errorResId = null
        )
    }

    fun onBiographyChange(biography: String) {
        val error = AuthorValidation.validateBiography(biography).errorResIdOrNull()
        _uiState.value = _uiState.value.copy(
            biography = biography,
            biographyError = error,
            errorResId = null
        )
    }

    fun onEmailChange(email: String) {
        val error = AuthorValidation.validateEmail(email).errorResIdOrNull()
        _uiState.value = _uiState.value.copy(
            email = email,
            emailError = error,
            errorResId = null
        )
    }

    fun onWebsiteChange(website: String) {
        val error = AuthorValidation.validateWebsite(website).errorResIdOrNull()
        _uiState.value = _uiState.value.copy(
            website = website,
            websiteError = error,
            errorResId = null
        )
    }

    fun validateAllFields() {
        val currentState = _uiState.value
        val fullNameError = AuthorValidation.validateFullName(currentState.fullName).errorResIdOrNull()
        val pseudonymError = AuthorValidation.validatePseudonym(currentState.pseudonym).errorResIdOrNull()
        val biographyError = AuthorValidation.validateBiography(currentState.biography).errorResIdOrNull()
        val emailError = AuthorValidation.validateEmail(currentState.email).errorResIdOrNull()
        val websiteError = AuthorValidation.validateWebsite(currentState.website).errorResIdOrNull()

        _uiState.value = currentState.copy(
            fullNameError = fullNameError,
            pseudonymError = pseudonymError,
            biographyError = biographyError,
            emailError = emailError,
            websiteError = websiteError
        )
    }

    fun createAuthor() {
        validateAllFields()
        if (!_uiState.value.isFormValid) return

        val currentState = _uiState.value
        viewModelScope.launch {
            _uiState.value = currentState.copy(isLoading = true, errorResId = null)

            val request = CreateAuthorRequest(
                fullName = currentState.fullName.trim(),
                pseudonym = currentState.pseudonym.trim().ifBlank { null },
                biography = currentState.biography.trim().ifBlank { null },
                email = currentState.email.trim().ifBlank { null },
                website = currentState.website.trim().ifBlank { null }
            )

            when (val result = authorsRepository.createAuthor(request)) {
                is DomainResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        createdAuthorId = result.data.id
                    )
                }
                is DomainResult.Error -> {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    handleError(result.type)
                }
            }
        }
    }

    private fun handleError(errorType: DomainErrorType) {
        if (errorType == DomainErrorType.UNAUTHORIZED) {
            _uiState.value = _uiState.value.copy(sessionExpired = true)
        } else {
            _uiState.value = _uiState.value.copy(errorResId = errorType.toStringResId())
        }
    }

    private fun ValidationResult.errorResIdOrNull(): Int? = when (this) {
        is ValidationResult.Valid -> null
        is ValidationResult.Error -> messageResId
    }

    fun resetState() {
        _uiState.value = CreateAuthorUiState()
    }
}
