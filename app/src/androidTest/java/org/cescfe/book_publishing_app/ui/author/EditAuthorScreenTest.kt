package org.cescfe.book_publishing_app.ui.author

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme
import org.junit.Rule
import org.junit.Test

class EditAuthorScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun editAuthorScreen_displaysTopBarAndBottomBar() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                EditAuthorScreenContent(
                    uiState = EditAuthorUiState(),
                    onNavigateUp = {},
                    onFullNameChange = {},
                    onPseudonymChange = {},
                    onBiographyChange = {},
                    onEmailChange = {},
                    onWebsiteChange = {},
                    onSaveClicked = {},
                    onDismissDialog = {},
                    onConfirmUpdateAuthor = {}
                )
            }
        }

        composeTestRule
            .onNodeWithTag("edit_author_top_bar")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithTag("save_button")
            .assertIsDisplayed()
    }

    @Test
    fun editAuthorScreen_validForm_saveClick_showsConfirmationDialog() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                EditAuthorScreenContent(
                    uiState = EditAuthorUiState(
                        fullName = "J. R. R. Tolkien",
                        biography = "English writer",
                        showConfirmDialog = true
                    ),
                    onNavigateUp = {},
                    onFullNameChange = {},
                    onPseudonymChange = {},
                    onBiographyChange = {},
                    onEmailChange = {},
                    onWebsiteChange = {},
                    onSaveClicked = {},
                    onDismissDialog = {},
                    onConfirmUpdateAuthor = {}
                )
            }
        }

        composeTestRule
            .onNodeWithTag("confirmation_dialog")
            .assertIsDisplayed()
    }

    @Test
    fun editAuthorScreen_confirmSave_callsOnConfirmUpdateAuthor() {
        var confirmCalled = false

        composeTestRule.setContent {
            BookpublishingappTheme {
                EditAuthorScreenContent(
                    uiState = EditAuthorUiState(
                        fullName = "J. R. R. Tolkien",
                        showConfirmDialog = true
                    ),
                    onNavigateUp = {},
                    onFullNameChange = {},
                    onPseudonymChange = {},
                    onBiographyChange = {},
                    onEmailChange = {},
                    onWebsiteChange = {},
                    onSaveClicked = {},
                    onDismissDialog = {},
                    onConfirmUpdateAuthor = {
                        confirmCalled = true
                    }
                )
            }
        }

        composeTestRule
            .onNodeWithTag("confirm_button")
            .performClick()
        assert(confirmCalled)
    }

    @Test
    fun editAuthorScreen_displaysPrefilledFormFields() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                EditAuthorScreenContent(
                    uiState = EditAuthorUiState(
                        fullName = "J.R.R. Tolkien",
                        pseudonym = "Tolkien",
                        biography = "English writer and philologist",
                        email = "tolkien@example.com",
                        website = "https://tolkien.com"
                    ),
                    onNavigateUp = {},
                    onFullNameChange = {},
                    onPseudonymChange = {},
                    onBiographyChange = {},
                    onEmailChange = {},
                    onWebsiteChange = {},
                    onSaveClicked = {},
                    onDismissDialog = {},
                    onConfirmUpdateAuthor = {}
                )
            }
        }

        composeTestRule
            .onNodeWithTag("full_name_field")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithTag("pseudonym_field")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithTag("biography_field")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithTag("email_field")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithTag("website_field")
            .assertIsDisplayed()
    }

    @Test
    fun editAuthorScreen_showsLoadingState_whenLoading() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                EditAuthorScreenContent(
                    uiState = EditAuthorUiState(isLoading = true),
                    onNavigateUp = {},
                    onFullNameChange = {},
                    onPseudonymChange = {},
                    onBiographyChange = {},
                    onEmailChange = {},
                    onWebsiteChange = {},
                    onSaveClicked = {},
                    onDismissDialog = {},
                    onConfirmUpdateAuthor = {}
                )
            }
        }

        composeTestRule
            .onNodeWithTag("loading_indicator")
            .assertIsDisplayed()
    }

    @Test
    fun editAuthorScreen_dismissDialog_callsOnDismissDialog() {
        var dismissCalled = false

        composeTestRule.setContent {
            BookpublishingappTheme {
                EditAuthorScreenContent(
                    uiState = EditAuthorUiState(
                        fullName = "J. R. R. Tolkien",
                        showConfirmDialog = true
                    ),
                    onNavigateUp = {},
                    onFullNameChange = {},
                    onPseudonymChange = {},
                    onBiographyChange = {},
                    onEmailChange = {},
                    onWebsiteChange = {},
                    onSaveClicked = {},
                    onDismissDialog = {
                        dismissCalled = true
                    },
                    onConfirmUpdateAuthor = {}
                )
            }
        }

        composeTestRule
            .onNodeWithTag("dismiss_button")
            .performClick()
        assert(dismissCalled)
    }
}
