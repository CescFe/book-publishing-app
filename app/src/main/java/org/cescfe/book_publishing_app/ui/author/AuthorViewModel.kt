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
import org.cescfe.book_publishing_app.domain.author.model.Author
import org.cescfe.book_publishing_app.domain.author.repository.AuthorsRepository
import org.cescfe.book_publishing_app.domain.shared.DomainErrorType
import org.cescfe.book_publishing_app.domain.shared.DomainResult
import org.cescfe.book_publishing_app.ui.shared.toStringResId

data class AuthorUiState(
    val author: Author? = null,
    val isLoading: Boolean = false,
    @get:StringRes val errorResId: Int? = null,
    val sessionExpired: Boolean = false,
    val isDeleting: Boolean = false,
    val deleteSuccess: Boolean = false,
    val showDeleteDialog: Boolean = false
)

class AuthorViewModel(
    private val authorsRepository: AuthorsRepository = AuthorsRepositoryImpl(
        RetrofitClient.authorsApi
    )
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthorUiState())
    val uiState: StateFlow<AuthorUiState> = _uiState.asStateFlow()

    fun onSessionExpiredHandled() {
        _uiState.value = _uiState.value.copy(sessionExpired = false)
    }

    private var lastAuthorId: String? = null

    fun loadAuthor(authorId: String) {
        lastAuthorId = authorId
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorResId = null
            )

            when (val result = authorsRepository.getAuthorById(authorId)) {
                is DomainResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        author = result.data,
                        errorResId = null
                    )
                }
                is DomainResult.Error -> {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    handleError(result.type)
                }
            }
        }
    }

    fun onDeleteClicked() {
        _uiState.value = _uiState.value.copy(showDeleteDialog = true)
    }

    fun onDeleteDialogDismissed() {
        _uiState.value = _uiState.value.copy(showDeleteDialog = false)
    }

    fun onDeleteConfirmed() {
        deleteAuthor()
    }

    fun deleteAuthor() {
        val authorId = _uiState.value.author?.id ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isDeleting = true,
                errorResId = null,
                showDeleteDialog = false
            )

            when (val result = authorsRepository.deleteAuthorById(authorId)) {
                is DomainResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isDeleting = false,
                        deleteSuccess = true
                    )
                }
                is DomainResult.Error -> {
                    _uiState.value = _uiState.value.copy(isDeleting = false)
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

    fun retry() {
        val authorIdToRetry = lastAuthorId ?: _uiState.value.author?.id
        if (authorIdToRetry != null) {
            loadAuthor(authorIdToRetry)
        }
    }
}
