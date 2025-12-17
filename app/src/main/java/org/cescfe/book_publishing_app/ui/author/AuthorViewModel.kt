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
    val sessionExpired: Boolean = false
)

class AuthorViewModel(
    private val authorsRepository: AuthorsRepository = AuthorsRepositoryImpl(
        RetrofitClient.authorsApi
    )
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthorUiState())
    val uiState: StateFlow<AuthorUiState> = _uiState.asStateFlow()

    fun loadAuthor(authorId: String) {
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
        val currentAuthorId = _uiState.value.author?.id
        if (currentAuthorId != null) {
            loadAuthor(currentAuthorId)
        }
    }
}
