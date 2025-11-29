package org.cescfe.book_publishing_app.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SplashUiState(val isLoading: Boolean = true, val isReady: Boolean = false)

class SplashViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    companion object {
        private const val SPLASH_DELAY_MS = 2000L
    }

    init {
        startSplashTimer()
    }

    private fun startSplashTimer() {
        viewModelScope.launch {
            delay(SPLASH_DELAY_MS)
            _uiState.value = SplashUiState(isLoading = false, isReady = true)
        }
    }
}
