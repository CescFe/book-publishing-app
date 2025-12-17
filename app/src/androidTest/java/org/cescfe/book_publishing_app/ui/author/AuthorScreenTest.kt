package org.cescfe.book_publishing_app.ui.author

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.cescfe.book_publishing_app.R
import org.cescfe.book_publishing_app.domain.author.model.Author
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme
import org.junit.Rule
import org.junit.Test

class AuthorScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // ==================== SMOKE TEST ====================

    @Test
    fun authorScreen_rendersWithoutCrash() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                AuthorScreenContent(
                    uiState = AuthorUiState(),
                    onRetry = {},
                    onNavigateUp = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("author_screen").assertIsDisplayed()
    }

    // ==================== LOADING STATE ====================

    @Test
    fun authorScreen_showsLoadingIndicator_whenLoading() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                AuthorScreenContent(
                    uiState = AuthorUiState(isLoading = true),
                    onRetry = {},
                    onNavigateUp = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("author_screen_loading").assertIsDisplayed()
        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
    }

    // ==================== ERROR STATE ====================

    @Test
    fun authorScreen_showsErrorMessage_whenError() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                AuthorScreenContent(
                    uiState = AuthorUiState(errorResId = R.string.error_network),
                    onRetry = {},
                    onNavigateUp = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("author_screen_error").assertIsDisplayed()
        composeTestRule.onNodeWithTag("error_state").assertIsDisplayed()
        composeTestRule.onNodeWithText("Network error. Please check your connection.").assertIsDisplayed()
    }

    @Test
    fun authorScreen_retryButton_callsOnRetry() {
        var retryCalled = false

        composeTestRule.setContent {
            BookpublishingappTheme {
                AuthorScreenContent(
                    uiState = AuthorUiState(errorResId = R.string.error_network),
                    onRetry = { retryCalled = true },
                    onNavigateUp = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("retry_button").performClick()

        assert(retryCalled) { "onRetry should have been called" }
    }

    // ==================== SUCCESS STATE ====================

    @Test
    fun authorScreen_showsAuthorCard_whenAuthorLoaded() {
        val author = Author(
            id = "author-123",
            fullName = "J.R.R. Tolkien",
            pseudonym = "Tolkien",
            biography = "English writer",
            email = "tolkien@example.com",
            website = "https://www.tolkienestate.com"
        )

        composeTestRule.setContent {
            BookpublishingappTheme {
                AuthorScreenContent(
                    uiState = AuthorUiState(author = author),
                    onRetry = {},
                    onNavigateUp = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("author_card_container").assertIsDisplayed()
        composeTestRule.onNodeWithTag("author_card_author-123").assertIsDisplayed()
    }

    @Test
    fun authorScreen_showsAuthorName_whenAuthorLoaded() {
        val author = Author(
            id = "author-123",
            fullName = "J.R.R. Tolkien",
            pseudonym = "Tolkien",
            biography = "English writer",
            email = "tolkien@example.com",
            website = "https://www.tolkienestate.com"
        )

        composeTestRule.setContent {
            BookpublishingappTheme {
                AuthorScreenContent(
                    uiState = AuthorUiState(author = author),
                    onRetry = {},
                    onNavigateUp = {}
                )
            }
        }

        composeTestRule.onNodeWithText("J.R.R. Tolkien").assertIsDisplayed()
    }

    // ==================== NAVIGATION ====================

    @Test
    fun authorScreen_callsOnNavigateUp_whenBackButtonClicked() {
        var navigateUpCalled = false

        composeTestRule.setContent {
            BookpublishingappTheme {
                AuthorScreenContent(
                    uiState = AuthorUiState(),
                    onRetry = {},
                    onNavigateUp = { navigateUpCalled = true }
                )
            }
        }

        composeTestRule.onNodeWithTag("back_button").performClick()

        assert(navigateUpCalled) {
            "onNavigateUp should have been called when back button is clicked"
        }
    }

    // ==================== BOTTOM BAR ====================

    @Test
    fun authorScreen_showsDetailActionsBottomBar() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                AuthorScreenContent(
                    uiState = AuthorUiState(),
                    onRetry = {},
                    onNavigateUp = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("detail_actions_bottom_bar").assertIsDisplayed()
    }
}
