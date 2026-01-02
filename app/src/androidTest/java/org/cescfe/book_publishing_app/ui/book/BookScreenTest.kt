package org.cescfe.book_publishing_app.ui.book

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.cescfe.book_publishing_app.R
import org.cescfe.book_publishing_app.data.auth.TokenManager
import org.cescfe.book_publishing_app.domain.auth.model.AuthToken
import org.cescfe.book_publishing_app.domain.book.model.Book
import org.cescfe.book_publishing_app.domain.book.model.enums.Status
import org.cescfe.book_publishing_app.domain.shared.enums.ReadingLevel
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BookScreenTest {

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
    fun bookScreen_rendersWithoutCrash() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                BookScreenContent(
                    uiState = BookUiState(),
                    onRetry = {},
                    onNavigateUp = {},
                    onEditClick = {},
                    onDeleteClick = {},
                    onDeleteDialogDismissed = {},
                    onDeleteConfirmed = {}
                )
            }
        }

        composeTestRule
            .onNodeWithTag("book_screen")
            .assertIsDisplayed()
    }

    // ==================== LOADING STATE ====================

    @Test
    fun bookScreen_showsLoadingIndicator_whenLoading() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                BookScreenContent(
                    uiState = BookUiState(isLoading = true),
                    onRetry = {},
                    onNavigateUp = {},
                    onEditClick = {},
                    onDeleteClick = {},
                    onDeleteDialogDismissed = {},
                    onDeleteConfirmed = {}
                )
            }
        }

        composeTestRule
            .onNodeWithTag("loading_indicator")
            .assertIsDisplayed()
    }

    // ==================== ERROR STATE ====================

    @Test
    fun bookScreen_callsOnRetry_whenRetryClicked() {
        var retryCalled = false

        composeTestRule.setContent {
            BookpublishingappTheme {
                BookScreenContent(
                    uiState = BookUiState(errorResId = R.string.error_network),
                    onRetry = { retryCalled = true },
                    onNavigateUp = {},
                    onEditClick = {},
                    onDeleteClick = {},
                    onDeleteDialogDismissed = {},
                    onDeleteConfirmed = {}
                )
            }
        }

        composeTestRule
            .onNodeWithTag("retry_button")
            .performClick()
        assert(retryCalled)
    }

    // ==================== SUCCESS STATE ====================

    @Test
    fun bookScreen_showsBookData_whenBookLoaded() {
        val book = Book(
            id = "book-123",
            title = "Test Book",
            basePrice = 10.0,
            authorName = "Test Author",
            collectionName = "Test Collection",
            readingLevel = ReadingLevel.ADULT,
            primaryLanguage = null,
            secondaryLanguages = emptyList(),
            primaryGenre = null,
            secondaryGenres = emptyList(),
            vatRate = 0.04,
            finalPrice = 10.40,
            isbn = null,
            publicationDate = null,
            pageCount = null,
            description = null,
            status = Status.DRAFT
        )

        composeTestRule.setContent {
            BookpublishingappTheme {
                BookScreenContent(
                    uiState = BookUiState(book = book),
                    onRetry = {},
                    onNavigateUp = {},
                    onEditClick = {},
                    onDeleteClick = {},
                    onDeleteDialogDismissed = {},
                    onDeleteConfirmed = {}
                )
            }
        }

        composeTestRule
            .onNodeWithText("Test Book")
            .assertIsDisplayed()
    }

    // ==================== DELETE DIALOG ====================

    @Test
    fun bookScreen_showsDeleteDialog_whenStateIsTrue() {
        val book = Book(
            id = "book-123",
            title = "Test Book",
            basePrice = 10.0,
            authorName = "Test Author",
            collectionName = "Test Collection",
            readingLevel = null,
            primaryLanguage = null,
            secondaryLanguages = emptyList(),
            primaryGenre = null,
            secondaryGenres = emptyList(),
            vatRate = 0.04,
            finalPrice = 10.40,
            isbn = null,
            publicationDate = null,
            pageCount = null,
            description = null,
            status = Status.DRAFT
        )

        composeTestRule.setContent {
            BookpublishingappTheme {
                BookScreenContent(
                    uiState = BookUiState(
                        book = book,
                        showDeleteDialog = true
                    ),
                    onRetry = {},
                    onNavigateUp = {},
                    onEditClick = {},
                    onDeleteClick = {},
                    onDeleteDialogDismissed = {},
                    onDeleteConfirmed = {}
                )
            }
        }

        composeTestRule
            .onNodeWithTag("confirmation_dialog")
            .assertIsDisplayed()
    }

    // ==================== NAVIGATION ====================

    @Test
    fun bookScreen_callsOnNavigateUp_whenBackClicked() {
        var navigateUpCalled = false

        composeTestRule.setContent {
            BookpublishingappTheme {
                BookScreenContent(
                    uiState = BookUiState(),
                    onRetry = {},
                    onNavigateUp = { navigateUpCalled = true },
                    onEditClick = {},
                    onDeleteClick = {}
                )
            }
        }

        composeTestRule
            .onNodeWithTag("back_button")
            .performClick()
        assert(navigateUpCalled)
    }

    // ==================== PERMISSIONS ====================

    @Test
    fun bookScreen_showsDetailActionsBottomBar_whenUserIsAdmin() {
        val adminToken = AuthToken(
            accessToken = "admin_token",
            tokenType = "Bearer",
            expiresIn = 3600,
            scope = "read write delete",
            userId = "admin123"
        )
        TokenManager.saveAuthToken(adminToken)

        val book = Book(
            id = "book-123",
            title = "Test Book",
            basePrice = 10.0,
            authorName = "Test Author",
            collectionName = "Test Collection",
            readingLevel = null,
            primaryLanguage = null,
            secondaryLanguages = emptyList(),
            primaryGenre = null,
            secondaryGenres = emptyList(),
            vatRate = 0.04,
            finalPrice = 10.40,
            isbn = null,
            publicationDate = null,
            pageCount = null,
            description = null,
            status = Status.DRAFT
        )

        composeTestRule.setContent {
            BookpublishingappTheme {
                BookScreenContent(
                    uiState = BookUiState(book = book),
                    onRetry = {},
                    onNavigateUp = {},
                    onEditClick = {},
                    onDeleteClick = {},
                    onDeleteDialogDismissed = {},
                    onDeleteConfirmed = {}
                )
            }
        }

        composeTestRule
            .onNodeWithTag("detail_actions_bottom_bar")
            .assertIsDisplayed()
    }

    @Test
    fun bookScreen_hidesDetailActionsBottomBar_whenUserIsReadOnly() {
        val readOnlyToken = AuthToken(
            accessToken = "readonly_token",
            tokenType = "Bearer",
            expiresIn = 3600,
            scope = "read",
            userId = "user123"
        )
        TokenManager.saveAuthToken(readOnlyToken)

        val book = Book(
            id = "book-123",
            title = "Test Book",
            basePrice = 10.0,
            authorName = "Test Author",
            collectionName = "Test Collection",
            readingLevel = null,
            primaryLanguage = null,
            secondaryLanguages = emptyList(),
            primaryGenre = null,
            secondaryGenres = emptyList(),
            vatRate = 0.04,
            finalPrice = 10.40,
            isbn = null,
            publicationDate = null,
            pageCount = null,
            description = null,
            status = Status.DRAFT
        )

        composeTestRule.setContent {
            BookpublishingappTheme {
                BookScreenContent(
                    uiState = BookUiState(book = book),
                    onRetry = {},
                    onNavigateUp = {},
                    onEditClick = {},
                    onDeleteClick = {},
                    onDeleteDialogDismissed = {},
                    onDeleteConfirmed = {}
                )
            }
        }

        composeTestRule
            .onNodeWithTag("detail_actions_bottom_bar")
            .assertDoesNotExist()
    }

    @Test
    fun bookScreen_hidesDetailActionsBottomBar_whenNoToken() {
        val book = Book(
            id = "book-123",
            title = "Test Book",
            basePrice = 10.0,
            authorName = "Test Author",
            collectionName = "Test Collection",
            readingLevel = null,
            primaryLanguage = null,
            secondaryLanguages = emptyList(),
            primaryGenre = null,
            secondaryGenres = emptyList(),
            vatRate = 0.04,
            finalPrice = 10.40,
            isbn = null,
            publicationDate = null,
            pageCount = null,
            description = null,
            status = Status.DRAFT
        )

        composeTestRule.setContent {
            BookpublishingappTheme {
                BookScreenContent(
                    uiState = BookUiState(book = book),
                    onRetry = {},
                    onNavigateUp = {},
                    onEditClick = {},
                    onDeleteClick = {},
                    onDeleteDialogDismissed = {},
                    onDeleteConfirmed = {}
                )
            }
        }

        composeTestRule
            .onNodeWithTag("detail_actions_bottom_bar")
            .assertDoesNotExist()
    }
}
