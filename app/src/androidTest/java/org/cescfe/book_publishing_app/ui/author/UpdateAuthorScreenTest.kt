package org.cescfe.book_publishing_app.ui.author

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme
import org.junit.Rule
import org.junit.Test

class UpdateAuthorScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun updateAuthorScreen_displaysTopBarAndBottomBar() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                UpdateAuthorScreenContent(
                    uiState = UpdateAuthorUiState(),
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
            .onNodeWithTag("update_author_top_bar")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithTag("save_button")
            .assertIsDisplayed()
    }

    @Test
    fun updateAuthorScreen_validForm_saveClick_showsConfirmationDialog() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                UpdateAuthorScreenContent(
                    uiState = UpdateAuthorUiState(
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
    fun updateAuthorScreen_confirmSave_callsOnConfirmUpdateAuthor() {
        var confirmCalled = false

        composeTestRule.setContent {
            BookpublishingappTheme {
                UpdateAuthorScreenContent(
                    uiState = UpdateAuthorUiState(
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
    fun updateAuthorScreen_displaysPrefilledFormFields() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                UpdateAuthorScreenContent(
                    uiState = UpdateAuthorUiState(
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
    fun updateAuthorScreen_showsLoadingState_whenLoading() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                UpdateAuthorScreenContent(
                    uiState = UpdateAuthorUiState(isLoading = true),
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
    fun updateAuthorScreen_dismissDialog_callsOnDismissDialog() {
        var dismissCalled = false

        composeTestRule.setContent {
            BookpublishingappTheme {
                UpdateAuthorScreenContent(
                    uiState = UpdateAuthorUiState(
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
