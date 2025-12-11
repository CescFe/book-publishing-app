package org.cescfe.book_publishing_app.ui.books

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.cescfe.book_publishing_app.data.auth.remote.RetrofitClient
import org.cescfe.book_publishing_app.data.book.repository.BooksRepositoryImpl
import org.cescfe.book_publishing_app.domain.book.model.Book
import org.cescfe.book_publishing_app.domain.book.model.BooksErrorType
import org.cescfe.book_publishing_app.domain.book.model.BooksResult
import org.cescfe.book_publishing_app.domain.book.repository.BooksRepository

data class BooksUiState(val books: List<Book> = emptyList(), val isLoading: Boolean = false, val error: String? = null, val sessionExpired: Boolean = false)

class BooksViewModel(private val booksRepository: BooksRepository = BooksRepositoryImpl(RetrofitClient.booksApi)) :
    ViewModel() {

    private val _uiState = MutableStateFlow(BooksUiState())
    val uiState: StateFlow<BooksUiState> = _uiState.asStateFlow()

    init {
        loadBooks()
    }

    fun loadBooks() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            when (val result = booksRepository.getBooks()) {
                is BooksResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        books = result.data,
                        error = null
                    )
                }
                is BooksResult.Error -> {
                    if (result.type == BooksErrorType.UNAUTHORIZED) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            sessionExpired = true
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            }
        }
    }

    fun retry() {
        loadBooks()
    }
}
