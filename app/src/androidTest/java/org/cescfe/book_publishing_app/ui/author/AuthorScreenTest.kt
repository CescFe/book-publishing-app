package org.cescfe.book_publishing_app.ui.author

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.cescfe.book_publishing_app.R
import org.cescfe.book_publishing_app.data.auth.TokenManager
import org.cescfe.book_publishing_app.domain.auth.model.AuthToken
import org.cescfe.book_publishing_app.domain.author.model.Author
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AuthorScreenTest {

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
    fun authorScreen_rendersWithoutCrash() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                AuthorScreenContent(
                    uiState = AuthorUiState(),
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
            .onNodeWithTag("author_screen")
            .assertIsDisplayed()
    }

    // ==================== LOADING STATE ====================

    @Test
    fun authorScreen_showsLoadingIndicator_whenLoading() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                AuthorScreenContent(
                    uiState = AuthorUiState(isLoading = true),
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
    fun authorScreen_callsOnRetry_whenRetryClicked() {
        var retryCalled = false

        composeTestRule.setContent {
            BookpublishingappTheme {
                AuthorScreenContent(
                    uiState = AuthorUiState(errorResId = R.string.error_network),
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
    fun authorScreen_showsAuthorData_whenAuthorLoaded() {
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
                    onNavigateUp = {},
                    onEditClick = {},
                    onDeleteClick = {},
                    onDeleteDialogDismissed = {},
                    onDeleteConfirmed = {}
                )
            }
        }

        composeTestRule
            .onNodeWithText("J.R.R. Tolkien")
            .assertIsDisplayed()
    }

    // ==================== DELETE DIALOG ====================

    @Test
    fun authorScreen_showsDeleteDialog_whenStateIsTrue() {
        val author = Author(
            id = "author-123",
            fullName = "J.R.R. Tolkien",
            pseudonym = null,
            biography = null,
            email = null,
            website = null
        )

        composeTestRule.setContent {
            BookpublishingappTheme {
                AuthorScreenContent(
                    uiState = AuthorUiState(
                        author = author,
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
    fun authorScreen_callsOnNavigateUp_whenBackClicked() {
        var navigateUpCalled = false

        composeTestRule.setContent {
            BookpublishingappTheme {
                AuthorScreenContent(
                    uiState = AuthorUiState(),
                    onRetry = {},
                    onNavigateUp = { navigateUpCalled = true },
                    onEditClick = {},
                    onDeleteClick = {},
                    onDeleteDialogDismissed = {},
                    onDeleteConfirmed = {}
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
    fun authorScreen_showsDetailActionsBottomBar_whenUserIsAdmin() {
        val adminToken = AuthToken(
            accessToken = "admin_token",
            tokenType = "Bearer",
            expiresIn = 3600,
            scope = "read write delete",
            userId = "admin123"
        )
        TokenManager.saveAuthToken(adminToken)

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
    fun authorScreen_hidesDetailActionsBottomBar_whenUserIsReadOnly() {
        val readOnlyToken = AuthToken(
            accessToken = "readonly_token",
            tokenType = "Bearer",
            expiresIn = 3600,
            scope = "read",
            userId = "user123"
        )
        TokenManager.saveAuthToken(readOnlyToken)

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
    fun authorScreen_hidesDetailActionsBottomBar_whenNoToken() {
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
