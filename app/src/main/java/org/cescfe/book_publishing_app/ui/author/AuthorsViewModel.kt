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
import org.cescfe.book_publishing_app.domain.author.model.AuthorSummary
import org.cescfe.book_publishing_app.domain.author.repository.AuthorsRepository
import org.cescfe.book_publishing_app.domain.shared.DomainErrorType
import org.cescfe.book_publishing_app.domain.shared.DomainResult
import org.cescfe.book_publishing_app.ui.shared.toStringResId

data class AuthorsUiState(
    val authorSummaries: List<AuthorSummary> = emptyList(),
    val isLoading: Boolean = false,
    @get:StringRes val errorResId: Int? = null,
    val sessionExpired: Boolean = false
)

class AuthorsViewModel(
    private val authorsRepository: AuthorsRepository = AuthorsRepositoryImpl(
        RetrofitClient.authorsApi
    )
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthorsUiState())
    val uiState: StateFlow<AuthorsUiState> = _uiState.asStateFlow()

    init {
        loadAuthors()
    }

    private fun loadAuthors() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorResId = null
            )

            when (val result = authorsRepository.getAuthors()) {
                is DomainResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        authorSummaries = result.data,
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
        loadAuthors()
    }
}
