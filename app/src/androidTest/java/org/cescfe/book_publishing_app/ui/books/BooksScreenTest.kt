package org.cescfe.book_publishing_app.ui.books

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.cescfe.book_publishing_app.domain.book.model.Book
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme
import org.junit.Rule
import org.junit.Test

class BooksScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

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

        composeTestRule.onNodeWithTag("books_screen").assertIsDisplayed()
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

        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
    }

    // ==================== ERROR STATE ====================

    @Test
    fun booksScreen_showsErrorMessage_whenError() {
        val errorMessage = "Network error. Please check your connection."

        composeTestRule.setContent {
            BookpublishingappTheme {
                BooksScreenContent(
                    uiState = BooksUiState(error = errorMessage),
                    onRetry = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("error_state").assertIsDisplayed()
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun booksScreen_showsRetryButton_whenError() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                BooksScreenContent(
                    uiState = BooksUiState(error = "Some error"),
                    onRetry = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("retry_button").assertIsDisplayed()
    }

    @Test
    fun booksScreen_retryButton_callsOnRetry() {
        var retryCalled = false

        composeTestRule.setContent {
            BookpublishingappTheme {
                BooksScreenContent(
                    uiState = BooksUiState(error = "Some error"),
                    onRetry = { retryCalled = true }
                )
            }
        }

        composeTestRule.onNodeWithTag("retry_button").performClick()

        assert(retryCalled) { "onRetry should have been called" }
    }

    // ==================== EMPTY STATE ====================

    @Test
    fun booksScreen_showsEmptyState_whenNoBooksAndNotLoading() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                BooksScreenContent(
                    uiState = BooksUiState(books = emptyList(), isLoading = false),
                    onRetry = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("empty_state").assertIsDisplayed()
    }

    // ==================== SUCCESS STATE ====================

    @Test
    fun booksScreen_showsBooksList_whenBooksAvailable() {
        val books = listOf(
            Book(
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
                    uiState = BooksUiState(books = books),
                    onRetry = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("books_list").assertIsDisplayed()
    }

    @Test
    fun booksScreen_showsBookTitle_whenBooksAvailable() {
        val books = listOf(
            Book(
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
                    uiState = BooksUiState(books = books),
                    onRetry = {}
                )
            }
        }

        composeTestRule.onNodeWithText("The Lord of the Rings").assertIsDisplayed()
    }
}
