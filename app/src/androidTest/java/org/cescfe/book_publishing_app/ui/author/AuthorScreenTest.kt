package org.cescfe.book_publishing_app.ui.author

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme
import org.junit.Rule
import org.junit.Test

class AuthorScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // ==================== NAVIGATION ====================

    @Test
    fun authorScreen_callsOnNavigateUp_whenBackButtonClicked() {
        var navigateUpCalled = false

        composeTestRule.setContent {
            BookpublishingappTheme {
                AuthorScreen(
                    authorId = "test-id",
                    onSessionExpired = {},
                    onNavigateUp = { navigateUpCalled = true }
                )
            }
        }

        composeTestRule.onNodeWithTag("back_button").performClick()

        assert(navigateUpCalled) {
            "onNavigateUp should have been called when back button is clicked"
        }
    }
}