package org.cescfe.book_publishing_app.ui.book

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.cescfe.book_publishing_app.data.book.repository.BooksRepositoryImpl
import org.cescfe.book_publishing_app.data.shared.remote.RetrofitClient
import org.cescfe.book_publishing_app.domain.book.model.Book
import org.cescfe.book_publishing_app.domain.book.repository.BooksRepository
import org.cescfe.book_publishing_app.domain.shared.DomainErrorType
import org.cescfe.book_publishing_app.domain.shared.DomainResult
import org.cescfe.book_publishing_app.ui.shared.toStringResId

data class BookUiState(
    val book: Book? = null,
    val isLoading: Boolean = false,
    @get:StringRes val errorResId: Int? = null,
    val sessionExpired: Boolean = false,
    val isDeleting: Boolean = false,
    val deleteSuccess: Boolean = false,
    val showDeleteDialog: Boolean = false
)

class BookViewModel(
    private val booksRepository: BooksRepository = BooksRepositoryImpl(
        RetrofitClient.booksApi
    )
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookUiState())
    val uiState: StateFlow<BookUiState> = _uiState.asStateFlow()

    private var lastBookId: String? = null

    fun onSessionExpiredHandled() {
        _uiState.value = _uiState.value.copy(sessionExpired = false)
    }

    fun loadBook(bookId: String) {
        lastBookId = bookId
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorResId = null
            )

            when (val result = booksRepository.getBookById(bookId)) {
                is DomainResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        book = result.data,
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
        deleteBook()
    }

    fun deleteBook() {
        val bookId = _uiState.value.book?.id ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isDeleting = true,
                errorResId = null,
                showDeleteDialog = false
            )

            when (val result = booksRepository.deleteBookById(bookId)) {
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
        val bookIdToRetry = lastBookId ?: _uiState.value.book?.id
        if (bookIdToRetry != null) {
            loadBook(bookIdToRetry)
        }
    }
}
