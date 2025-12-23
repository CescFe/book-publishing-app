package org.cescfe.book_publishing_app.ui.author

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
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
    fun createAuthorScreen_confirmSave_callsCreateAuthor() {
        var createCalled = false

        composeTestRule.setContent {
            BookpublishingappTheme {
                CreateAuthorScreen(
                    onAuthorCreated = { createCalled = true }
                )
            }
        }

        composeTestRule
            .onNodeWithTag("full_name_field")
            .performTextInput("J. R. R. Tolkien")
        composeTestRule
            .onNodeWithTag("save_button")
            .performClick()
        composeTestRule
            .onNodeWithTag("confirm_button")
            .performClick()
        assert(createCalled)
    }

    @Test
    fun createAuthorScreen_invalidForm_saveClick_doesNotShowConfirmationDialog() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                CreateAuthorScreen()
            }
        }

        composeTestRule
            .onNodeWithTag("save_button")
            .performClick()
        composeTestRule
            .onNodeWithTag("confirmation_dialog")
            .assertDoesNotExist()
        composeTestRule
            .onNodeWithTag("full_name_error")
            .assertIsDisplayed()
    }
}
