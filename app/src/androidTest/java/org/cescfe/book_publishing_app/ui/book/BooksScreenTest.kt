package org.cescfe.book_publishing_app.ui.book

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.cescfe.book_publishing_app.R
import org.cescfe.book_publishing_app.data.auth.TokenManager
import org.cescfe.book_publishing_app.domain.auth.model.AuthToken
import org.cescfe.book_publishing_app.domain.book.model.BookSummary
import org.cescfe.book_publishing_app.ui.shared.navigation.BottomNavItem
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BooksScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        TokenManager.clearToken()
    }

    @After
    fun tearDown() {
        TokenManager.clearToken()
    }

    // ==================== SMOKE TEST ====================

    @Test
    fun booksScreen_rendersWithoutCrash() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                BooksScreenContent(
                    uiState = BooksUiState(),
                    onRetry = {}
                )
            }
        }

        composeTestRule
            .onNodeWithTag("books_screen")
            .assertIsDisplayed()
    }

    // ==================== LOADING STATE ====================

    @Test
    fun booksScreen_showsLoadingIndicator_whenLoading() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                BooksScreenContent(
                    uiState = BooksUiState(isLoading = true),
                    onRetry = {}
                )
            }
        }

        composeTestRule
            .onNodeWithTag("loading_indicator")
            .assertIsDisplayed()
    }

    // ==================== ERROR STATE ====================

    @Test
    fun booksScreen_errorState_displaysMessage_andRetryWorks() {
        var retryCalled = false

        composeTestRule.setContent {
            BookpublishingappTheme {
                BooksScreenContent(
                    uiState = BooksUiState(errorResId = R.string.error_network),
                    onRetry = { retryCalled = true }
                )
            }
        }

        composeTestRule
            .onNodeWithTag("error_state")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithTag("retry_button")
            .performClick()
        assert(retryCalled)
    }

    // ==================== EMPTY STATE ====================

    @Test
    fun booksScreen_showsEmptyState_whenNoBooksAndNotLoading() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                BooksScreenContent(
                    uiState = BooksUiState(bookSummaries = emptyList(), isLoading = false),
                    onRetry = {}
                )
            }
        }

        composeTestRule
            .onNodeWithTag("empty_state")
            .assertIsDisplayed()
    }

    // ==================== SUCCESS STATE ====================

    @Test
    fun booksScreen_showsBooksList_whenBooksAvailable() {
        val bookSummaries = listOf(
            BookSummary(
                id = "1",
                title = "The Lord of the Rings",
                author = "J.R.R. Tolkien",
                collection = "Fantasy",
                finalPrice = 29.99,
                isbn = "978-0-544-00001-0"
            )
        )

        composeTestRule.setContent {
            BookpublishingappTheme {
                BooksScreenContent(
                    uiState = BooksUiState(bookSummaries = bookSummaries),
                    onRetry = {}
                )
            }
        }

        composeTestRule
            .onNodeWithTag("books_list")
            .assertIsDisplayed()
    }

    // ==================== BOTTOM NAVIGATION ====================

    @Test
    fun booksScreen_bottomBar_callsOnNavigate_whenCollectionsClicked() {
        var navigatedItem: BottomNavItem? = null

        composeTestRule.setContent {
            BookpublishingappTheme {
                BooksScreenContent(
                    uiState = BooksUiState(),
                    onRetry = {},
                    onNavigate = { item -> navigatedItem = item }
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Collections")
            .performClick()

        assert(navigatedItem == BottomNavItem.Collections) {
            "Should navigate to Collections, but got $navigatedItem"
        }
    }

    // ==================== PERMISSIONS - FAB ====================

    @Test
    fun booksScreen_showsCreateFab_whenUserIsAdmin() {
        val adminToken = AuthToken(
            accessToken = "admin_token",
            tokenType = "Bearer",
            expiresIn = 3600,
            scope = "read write delete",
            userId = "admin123"
        )
        TokenManager.saveAuthToken(adminToken)

        composeTestRule.setContent {
            BookpublishingappTheme {
                BooksScreenContent(
                    uiState = BooksUiState(),
                    onRetry = {}
                )
            }
        }

        composeTestRule
            .onNodeWithTag("create_book_fab")
            .assertIsDisplayed()
    }

    @Test
    fun booksScreen_hidesCreateFab_whenUserIsReadOnly() {
        val readOnlyToken = AuthToken(
            accessToken = "readonly_token",
            tokenType = "Bearer",
            expiresIn = 3600,
            scope = "read",
            userId = "user123"
        )
        TokenManager.saveAuthToken(readOnlyToken)

        composeTestRule.setContent {
            BookpublishingappTheme {
                BooksScreenContent(
                    uiState = BooksUiState(),
                    onRetry = {}
                )
            }
        }

        composeTestRule
            .onNodeWithTag("create_book_fab")
            .assertDoesNotExist()
    }

    @Test
    fun booksScreen_hidesCreateFab_whenNoToken() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                BooksScreenContent(
                    uiState = BooksUiState(),
                    onRetry = {}
                )
            }
        }

        composeTestRule
            .onNodeWithTag("create_book_fab")
            .assertDoesNotExist()
    }
}
