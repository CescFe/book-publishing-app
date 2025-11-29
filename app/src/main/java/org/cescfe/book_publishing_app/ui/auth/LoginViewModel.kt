package org.cescfe.book_publishing_app.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAuthenticated: Boolean = false
)

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onUsernameChange(username: String) {
        _uiState.value = _uiState.value.copy(
            username = username,
            error = null
        )
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password,
            error = null
        )
    }

    fun onLoginClick() {
        val currentState = _uiState.value

        if (!isFormValid(currentState.username, currentState.password)) {
            _uiState.value = currentState.copy(
                error = "Username and password are required"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = currentState.copy(
                isLoading = true,
                error = null
            )

            // TODO: Implement authentication with AuthRepository
            delay(1500)

            // Simulate success for now (just for UI testing)
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                isAuthenticated = true
            )
        }
    }

    private fun isFormValid(username: String, password: String): Boolean {
        return username.isNotBlank() && password.isNotBlank()
    }
}