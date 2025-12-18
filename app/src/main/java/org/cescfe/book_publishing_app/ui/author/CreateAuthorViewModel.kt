package org.cescfe.book_publishing_app.ui.author

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.cescfe.book_publishing_app.R
import org.cescfe.book_publishing_app.data.author.repository.AuthorsRepositoryImpl
import org.cescfe.book_publishing_app.data.shared.remote.RetrofitClient
import org.cescfe.book_publishing_app.domain.author.model.CreateAuthorRequest
import org.cescfe.book_publishing_app.domain.author.repository.AuthorsRepository
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
    @get:StringRes val errorResId: Int? = null,
    val sessionExpired: Boolean = false,
    val createdAuthorId: String? = null
)

class CreateAuthorViewModel(
    private val authorsRepository: AuthorsRepository = AuthorsRepositoryImpl(
        RetrofitClient.authorsApi
    )
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateAuthorUiState())
    val uiState: StateFlow<CreateAuthorUiState> = _uiState.asStateFlow()

    fun onFullNameChange(fullName: String) {
        _uiState.value = _uiState.value.copy(fullName = fullName, errorResId = null)
    }

    fun onPseudonymChange(pseudonym: String) {
        _uiState.value = _uiState.value.copy(pseudonym = pseudonym, errorResId = null)
    }

    fun onBiographyChange(biography: String) {
        _uiState.value = _uiState.value.copy(biography = biography, errorResId = null)
    }

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email, errorResId = null)
    }

    fun onWebsiteChange(website: String) {
        _uiState.value = _uiState.value.copy(website = website, errorResId = null)
    }

    fun createAuthor() {
        val currentState = _uiState.value

        if (currentState.fullName.isBlank()) {
            _uiState.value = currentState.copy(errorResId = R.string.error_full_name_required)
            return
        }

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

    fun resetState() {
        _uiState.value = CreateAuthorUiState()
    }
}
