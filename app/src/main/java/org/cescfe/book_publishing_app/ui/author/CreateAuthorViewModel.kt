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
    val createdAuthorId: String? = null,
    val showConfirmDialog: Boolean = false
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
        updateField(
            value = fullName,
            validator = AuthorValidation::validateFullName
        ) { state, error ->
            state.copy(fullName = fullName, fullNameError = error)
        }
    }

    fun onPseudonymChange(pseudonym: String) {
        updateField(
            value = pseudonym,
            validator = AuthorValidation::validatePseudonym
        ) { state, error ->
            state.copy(pseudonym = pseudonym, pseudonymError = error)
        }
    }

    fun onBiographyChange(biography: String) {
        updateField(
            value = biography,
            validator = AuthorValidation::validateBiography
        ) { state, error ->
            state.copy(biography = biography, biographyError = error)
        }
    }

    fun onEmailChange(email: String) {
        updateField(
            value = email,
            validator = AuthorValidation::validateEmail
        ) { state, error ->
            state.copy(email = email, emailError = error)
        }
    }

    fun onWebsiteChange(website: String) {
        updateField(
            value = website,
            validator = AuthorValidation::validateWebsite
        ) { state, error ->
            state.copy(website = website, websiteError = error)
        }
    }

    fun onSaveClicked() {
        validateAllFields()
        if (_uiState.value.isFormValid) {
            _uiState.value = _uiState.value.copy(showConfirmDialog = true)
        }
    }

    fun dismissConfirmDialog() {
        _uiState.value = _uiState.value.copy(showConfirmDialog = false)
    }

    fun createAuthor() {
        dismissConfirmDialog()
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

    private fun validateAllFields() {
        val s = _uiState.value
        _uiState.value = s.copy(
            fullNameError = AuthorValidation.validateFullName(s.fullName).errorResIdOrNull(),
            pseudonymError = AuthorValidation.validatePseudonym(s.pseudonym).errorResIdOrNull(),
            biographyError = AuthorValidation.validateBiography(s.biography).errorResIdOrNull(),
            emailError = AuthorValidation.validateEmail(s.email).errorResIdOrNull(),
            websiteError = AuthorValidation.validateWebsite(s.website).errorResIdOrNull()
        )
    }

    private fun handleError(errorType: DomainErrorType) {
        _uiState.value =
            if (errorType == DomainErrorType.UNAUTHORIZED) {
                _uiState.value.copy(sessionExpired = true)
            } else {
                _uiState.value.copy(errorResId = errorType.toStringResId())
            }
    }

    private fun ValidationResult.errorResIdOrNull(): Int? =
        when (this) {
            is ValidationResult.Valid -> null
            is ValidationResult.Error -> messageResId
        }

    private inline fun updateField(
        value: String,
        validator: (String) -> ValidationResult,
        crossinline reducer: (CreateAuthorUiState, Int?) -> CreateAuthorUiState
    ) {
        val error = validator(value).errorResIdOrNull()
        _uiState.value = reducer(_uiState.value, error).copy(errorResId = null)
    }
}
