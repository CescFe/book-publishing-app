package org.cescfe.book_publishing_app.ui.books

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.cescfe.book_publishing_app.R
import org.cescfe.book_publishing_app.data.book.repository.BooksRepositoryImpl
import org.cescfe.book_publishing_app.data.shared.remote.RetrofitClient
import org.cescfe.book_publishing_app.domain.book.model.BookSummary
import org.cescfe.book_publishing_app.domain.book.repository.BooksRepository
import org.cescfe.book_publishing_app.domain.shared.DomainErrorType
import org.cescfe.book_publishing_app.domain.shared.DomainResult

data class BooksUiState(
    val bookSummaries: List<BookSummary> = emptyList(),
    val isLoading: Boolean = false,
    @get:StringRes val errorResId: Int? = null,
    val sessionExpired: Boolean = false
)

class BooksViewModel(
    private val booksRepository: BooksRepository = BooksRepositoryImpl(RetrofitClient.booksApi)
) : ViewModel() {

    private val _uiState = MutableStateFlow(BooksUiState())
    val uiState: StateFlow<BooksUiState> = _uiState.asStateFlow()

    init {
        loadBooks()
    }

    fun loadBooks() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorResId = null
            )

            when (val result = booksRepository.getBooks()) {
                is DomainResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        bookSummaries = result.data,
                        errorResId = null
                    )
                }
                is DomainResult.Error -> {
                    if (result.type == DomainErrorType.UNAUTHORIZED) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            sessionExpired = true
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorResId = result.type.toStringResId()
                        )
                    }
                }
            }
        }
    }

    fun retry() {
        loadBooks()
    }

    private fun DomainErrorType.toStringResId(): Int = when (this) {
        DomainErrorType.TIMEOUT -> R.string.error_timeout
        DomainErrorType.NETWORK_ERROR -> R.string.error_network
        DomainErrorType.UNAUTHORIZED -> R.string.error_unauthorized
        DomainErrorType.SERVER_ERROR -> R.string.error_server
        DomainErrorType.UNKNOWN -> R.string.error_unknown
    }
}
