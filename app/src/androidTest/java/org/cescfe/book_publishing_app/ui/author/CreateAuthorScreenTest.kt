package org.cescfe.book_publishing_app.ui.author

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.cescfe.book_publishing_app.R
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme
import org.junit.Rule
import org.junit.Test

class CreateAuthorScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun createAuthorScreen_displaysTopBarAndBottomBar() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                CreateAuthorScreen()
            }
        }

        composeTestRule
            .onNodeWithTag("create_author_top_bar")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithTag("save_button")
            .assertIsDisplayed()
    }

    @Test
    fun createAuthorScreen_validForm_saveClick_showsConfirmationDialog() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                CreateAuthorScreen()
            }
        }

        composeTestRule
            .onNodeWithTag("full_name_field")
            .performTextInput("J. R. R. Tolkien")
        composeTestRule
            .onNodeWithTag("biography_field")
            .performTextInput("English writer")
        composeTestRule
            .onNodeWithTag("save_button")
            .performClick()
        composeTestRule
            .onNodeWithTag("confirmation_dialog")
            .assertIsDisplayed()
    }

    @Test
    fun createAuthorScreen_invalidForm_doesNotShowDialog() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                CreateAuthorScreenContent(
                    uiState = CreateAuthorUiState(
                        fullName = "",
                        fullNameError = R.string.error_full_name_required
                    ),
                    onNavigateUp = {},
                    onFullNameChange = {},
                    onPseudonymChange = {},
                    onBiographyChange = {},
                    onEmailChange = {},
                    onWebsiteChange = {},
                    onSaveClicked = {},
                    onDismissDialog = {},
                    onConfirmCreateAuthor = {}
                )
            }
        }

        composeTestRule
            .onNodeWithTag("confirmation_dialog")
            .assertDoesNotExist()
    }

    @Test
    fun createAuthorScreen_confirmSave_callsOnConfirmCreateAuthor() {
        var confirmCalled = false

        composeTestRule.setContent {
            BookpublishingappTheme {
                CreateAuthorScreenContent(
                    uiState = CreateAuthorUiState(
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
                    onConfirmCreateAuthor = {
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
}
