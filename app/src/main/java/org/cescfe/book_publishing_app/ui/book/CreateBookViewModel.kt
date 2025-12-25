package org.cescfe.book_publishing_app.ui.book

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CreateBookUiState(
    val isLoading: Boolean = false,
    val sessionExpired: Boolean = false,
    val createdBookId: String? = null,
    val showConfirmDialog: Boolean = false
)

class CreateBookViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CreateBookUiState())
    val uiState: StateFlow<CreateBookUiState> = _uiState.asStateFlow()

    fun onSessionExpiredHandled() {
        _uiState.value = _uiState.value.copy(sessionExpired = false)
    }

    fun onSaveClicked() {
        _uiState.value = _uiState.value.copy(showConfirmDialog = true)
    }

    fun dismissConfirmDialog() {
        _uiState.value = _uiState.value.copy(showConfirmDialog = false)
    }

    fun createBook() {
        // TODO: Implement book creation logic here
        _uiState.value = _uiState.value.copy(showConfirmDialog = false)
    }
}
