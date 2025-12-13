package org.cescfe.book_publishing_app.ui.author

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.cescfe.book_publishing_app.R
import org.cescfe.book_publishing_app.domain.author.model.AuthorSummary
import org.cescfe.book_publishing_app.ui.shared.navigation.BottomNavItem
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme
import org.junit.Rule
import org.junit.Test

class AuthorsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // ==================== SMOKE TEST ====================

    @Test
    fun authorsScreen_rendersWithoutCrash() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                AuthorsScreenContent(
                    uiState = AuthorsUiState(),
                    onRetry = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("authors_screen").assertIsDisplayed()
    }

    // ==================== LOADING STATE ====================

    @Test
    fun authorsScreen_showsLoadingIndicator_whenLoading() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                AuthorsScreenContent(
                    uiState = AuthorsUiState(isLoading = true),
                    onRetry = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
    }

    // ==================== ERROR STATE ====================

    @Test
    fun authorsScreen_showsErrorMessage_whenError() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                AuthorsScreenContent(
                    uiState = AuthorsUiState(errorResId = R.string.error_network),
                    onRetry = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("error_state").assertIsDisplayed()
        composeTestRule.onNodeWithText("Network error. Please check your connection.").assertIsDisplayed()
    }

    @Test
    fun authorsScreen_showsRetryButton_whenError() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                AuthorsScreenContent(
                    uiState = AuthorsUiState(errorResId = R.string.error_network),
                    onRetry = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("retry_button").assertIsDisplayed()
    }

    @Test
    fun authorsScreen_retryButton_callsOnRetry() {
        var retryCalled = false

        composeTestRule.setContent {
            BookpublishingappTheme {
                AuthorsScreenContent(
                    uiState = AuthorsUiState(errorResId = R.string.error_network),
                    onRetry = { retryCalled = true }
                )
            }
        }

        composeTestRule.onNodeWithTag("retry_button").performClick()

        assert(retryCalled) { "onRetry should have been called" }
    }

    // ==================== EMPTY STATE ====================

    @Test
    fun authorsScreen_showsEmptyState_whenNoAuthorsAndNotLoading() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                AuthorsScreenContent(
                    uiState = AuthorsUiState(authorSummaries = emptyList(), isLoading = false),
                    onRetry = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("empty_state").assertIsDisplayed()
    }

    // ==================== SUCCESS STATE ====================

    @Test
    fun authorsScreen_showsAuthorsList_whenAuthorsAvailable() {
        val authorSummaries = listOf(
            AuthorSummary(
                id = "1",
                fullName = "J.R.R. Tolkien",
                pseudonym = "Tolkien",
                email = "tolkien@example.com"
            )
        )

        composeTestRule.setContent {
            BookpublishingappTheme {
                AuthorsScreenContent(
                    uiState = AuthorsUiState(authorSummaries = authorSummaries),
                    onRetry = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("authors_list").assertIsDisplayed()
    }

    @Test
    fun authorsScreen_showsAuthorName_whenAuthorsAvailable() {
        val authorSummaries = listOf(
            AuthorSummary(
                id = "1",
                fullName = "J.R.R. Tolkien",
                pseudonym = "Tolkien",
                email = "tolkien@example.com"
            )
        )

        composeTestRule.setContent {
            BookpublishingappTheme {
                AuthorsScreenContent(
                    uiState = AuthorsUiState(authorSummaries = authorSummaries),
                    onRetry = {}
                )
            }
        }

        composeTestRule.onNodeWithText("J.R.R. Tolkien").assertIsDisplayed()
    }

    // ==================== BOTTOM NAVIGATION ====================

    @Test
    fun authorsScreen_showsBottomNavigationBar() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                AuthorsScreenContent(
                    uiState = AuthorsUiState(),
                    onRetry = {},
                    onNavigate = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("app_bottom_bar").assertIsDisplayed()
    }

    @Test
    fun authorsScreen_bottomBar_callsOnNavigate_whenBooksClicked() {
        var navigatedItem: BottomNavItem? = null

        composeTestRule.setContent {
            BookpublishingappTheme {
                AuthorsScreenContent(
                    uiState = AuthorsUiState(),
                    onRetry = {},
                    onNavigate = { item -> navigatedItem = item }
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Books").performClick()

        assert(navigatedItem == BottomNavItem.Books) {
            "Should navigate to Books, but got $navigatedItem"
        }
    }

    @Test
    fun authorsScreen_bottomBar_callsOnNavigate_whenCollectionsClicked() {
        var navigatedItem: BottomNavItem? = null

        composeTestRule.setContent {
            BookpublishingappTheme {
                AuthorsScreenContent(
                    uiState = AuthorsUiState(),
                    onRetry = {},
                    onNavigate = { item -> navigatedItem = item }
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Collections").performClick()

        assert(navigatedItem == BottomNavItem.Collections) {
            "Should navigate to Collections, but got $navigatedItem"
        }
    }
}
