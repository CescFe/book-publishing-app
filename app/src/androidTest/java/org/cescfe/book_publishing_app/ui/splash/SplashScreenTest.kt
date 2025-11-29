package org.cescfe.book_publishing_app.ui.splash

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import org.junit.Rule
import org.junit.Test

class SplashScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun splashScreen_displaysLoadingState() {
        composeTestRule.setContent {
            SplashScreen(
                uiState = SplashUiState(isLoading = true, isReady = false),
                onSplashFinished = {}
            )
        }

        composeTestRule.onNodeWithContentDescription("Logo").assertExists()
    }

    @Test
    fun splashScreen_callsOnSplashFinished_whenReady() {
        var onSplashFinishedCalled = false
        
        composeTestRule.setContent {
            SplashScreen(
                uiState = SplashUiState(isLoading = false, isReady = true),
                onSplashFinished = { onSplashFinishedCalled = true }
            )
        }

        // Since isReady is true, the LaunchedEffect should trigger immediately.
        composeTestRule.waitForIdle()
        assert(onSplashFinishedCalled)
    }
}
