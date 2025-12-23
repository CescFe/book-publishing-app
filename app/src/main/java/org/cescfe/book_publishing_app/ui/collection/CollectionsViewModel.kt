package org.cescfe.book_publishing_app.ui.collection

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.cescfe.book_publishing_app.data.collection.repository.CollectionsRepositoryImpl
import org.cescfe.book_publishing_app.data.shared.remote.RetrofitClient
import org.cescfe.book_publishing_app.domain.collection.model.CollectionSummary
import org.cescfe.book_publishing_app.domain.collection.repository.CollectionsRepository
import org.cescfe.book_publishing_app.domain.shared.DomainErrorType
import org.cescfe.book_publishing_app.domain.shared.DomainResult
import org.cescfe.book_publishing_app.ui.shared.toStringResId

data class CollectionsUiState(
    val collectionSummaries: List<CollectionSummary> = emptyList(),
    val isLoading: Boolean = false,
    @get:StringRes val errorResId: Int? = null,
    val sessionExpired: Boolean = false
)

class CollectionsViewModel(
    private val collectionsRepository: CollectionsRepository = CollectionsRepositoryImpl(
        RetrofitClient.collectionsApi
    )
) : ViewModel() {

    private val _uiState = MutableStateFlow(CollectionsUiState())
    val uiState: StateFlow<CollectionsUiState> = _uiState.asStateFlow()

    init {
        loadCollections()
    }

    fun onSessionExpiredHandled() {
        _uiState.value = _uiState.value.copy(sessionExpired = false)
    }

    private fun loadCollections() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorResId = null
            )

            when (val result = collectionsRepository.getCollections()) {
                is DomainResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        collectionSummaries = result.data,
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
        loadCollections()
    }
}
