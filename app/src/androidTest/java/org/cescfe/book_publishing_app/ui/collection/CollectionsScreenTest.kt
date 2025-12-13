package org.cescfe.book_publishing_app.ui.collection

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.cescfe.book_publishing_app.R
import org.cescfe.book_publishing_app.domain.collection.model.CollectionSummary
import org.cescfe.book_publishing_app.ui.shared.navigation.BottomNavItem
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme
import org.junit.Rule
import org.junit.Test

class CollectionsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // ==================== SMOKE TEST ====================

    @Test
    fun collectionsScreen_rendersWithoutCrash() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                CollectionsScreenContent(
                    uiState = CollectionsUiState(),
                    onRetry = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("collections_screen").assertIsDisplayed()
    }

    // ==================== LOADING STATE ====================

    @Test
    fun collectionsScreen_showsLoadingIndicator_whenLoading() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                CollectionsScreenContent(
                    uiState = CollectionsUiState(isLoading = true),
                    onRetry = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
    }

    // ==================== ERROR STATE ====================

    @Test
    fun collectionsScreen_showsErrorMessage_whenError() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                CollectionsScreenContent(
                    uiState = CollectionsUiState(errorResId = R.string.error_network),
                    onRetry = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("error_state").assertIsDisplayed()
        composeTestRule.onNodeWithText("Network error. Please check your connection.").assertIsDisplayed()
    }

    @Test
    fun collectionsScreen_showsRetryButton_whenError() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                CollectionsScreenContent(
                    uiState = CollectionsUiState(errorResId = R.string.error_network),
                    onRetry = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("retry_button").assertIsDisplayed()
    }

    @Test
    fun collectionsScreen_retryButton_callsOnRetry() {
        var retryCalled = false

        composeTestRule.setContent {
            BookpublishingappTheme {
                CollectionsScreenContent(
                    uiState = CollectionsUiState(errorResId = R.string.error_network),
                    onRetry = { retryCalled = true }
                )
            }
        }

        composeTestRule.onNodeWithTag("retry_button").performClick()

        assert(retryCalled) { "onRetry should have been called" }
    }

    // ==================== EMPTY STATE ====================

    @Test
    fun collectionsScreen_showsEmptyState_whenNoCollectionsAndNotLoading() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                CollectionsScreenContent(
                    uiState = CollectionsUiState(collectionSummaries = emptyList(), isLoading = false),
                    onRetry = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("empty_state").assertIsDisplayed()
    }

    // ==================== SUCCESS STATE ====================

    @Test
    fun collectionsScreen_showsCollectionsList_whenCollectionsAvailable() {
        val collectionSummaries = listOf(
            CollectionSummary(
                id = "1",
                name = "Fantasy Collection",
                readingLevel = "Advanced",
                primaryLanguage = "English",
                primaryGenre = "Fantasy"
            )
        )

        composeTestRule.setContent {
            BookpublishingappTheme {
                CollectionsScreenContent(
                    uiState = CollectionsUiState(collectionSummaries = collectionSummaries),
                    onRetry = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("collections_list").assertIsDisplayed()
    }

    @Test
    fun collectionsScreen_showsCollectionName_whenCollectionsAvailable() {
        val collectionSummaries = listOf(
            CollectionSummary(
                id = "1",
                name = "Fantasy Collection",
                readingLevel = "Advanced",
                primaryLanguage = "English",
                primaryGenre = "Fantasy"
            )
        )

        composeTestRule.setContent {
            BookpublishingappTheme {
                CollectionsScreenContent(
                    uiState = CollectionsUiState(collectionSummaries = collectionSummaries),
                    onRetry = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Fantasy Collection").assertIsDisplayed()
    }

    // ==================== BOTTOM NAVIGATION ====================

    @Test
    fun collectionsScreen_showsBottomNavigationBar() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                CollectionsScreenContent(
                    uiState = CollectionsUiState(),
                    onRetry = {},
                    onNavigate = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("app_bottom_bar").assertIsDisplayed()
    }

    @Test
    fun collectionsScreen_bottomBar_callsOnNavigate_whenBooksClicked() {
        var navigatedItem: BottomNavItem? = null

        composeTestRule.setContent {
            BookpublishingappTheme {
                CollectionsScreenContent(
                    uiState = CollectionsUiState(),
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
    fun collectionsScreen_bottomBar_callsOnNavigate_whenAuthorsClicked() {
        var navigatedItem: BottomNavItem? = null

        composeTestRule.setContent {
            BookpublishingappTheme {
                CollectionsScreenContent(
                    uiState = CollectionsUiState(),
                    onRetry = {},
                    onNavigate = { item -> navigatedItem = item }
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Authors").performClick()

        assert(navigatedItem == BottomNavItem.Authors) {
            "Should navigate to Authors, but got $navigatedItem"
        }
    }
}
