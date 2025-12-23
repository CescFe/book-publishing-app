package org.cescfe.book_publishing_app.ui.shared.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme
import org.junit.Rule
import org.junit.Test

class ConfirmationDialogTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // ==================== SMOKE TEST ====================

    @Test
    fun confirmationDialog_rendersWithoutCrash() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                ConfirmationDialog(
                    title = "Test Title",
                    message = "Test Message",
                    onDismiss = {},
                    onConfirm = {},
                    isVisible = true
                )
            }
        }

        composeTestRule
            .onNodeWithTag("confirmation_dialog")
            .assertIsDisplayed()
    }

    // ==================== VISIBILITY ====================

    @Test
    fun confirmationDialog_isNotDisplayed_whenNotVisible() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                ConfirmationDialog(
                    title = "Delete Author",
                    message = "Are you sure?",
                    onDismiss = {},
                    onConfirm = {},
                    isVisible = false
                )
            }
        }

        composeTestRule
            .onNodeWithTag("confirmation_dialog")
            .assertDoesNotExist()
    }

    // ==================== BUTTON INTERACTIONS ====================

    @Test
    fun confirmationDialog_dismissButton_callsOnDismiss() {
        var dismissCalled = false

        composeTestRule.setContent {
            BookpublishingappTheme {
                ConfirmationDialog(
                    title = "Test Title",
                    message = "Test Message",
                    onDismiss = { dismissCalled = true },
                    onConfirm = {},
                    isVisible = true
                )
            }
        }

        composeTestRule
            .onNodeWithTag("dismiss_button")
            .performClick()
        assert(dismissCalled)
    }

    @Test
    fun confirmationDialog_confirmButton_callsOnConfirm() {
        var confirmCalled = false

        composeTestRule.setContent {
            BookpublishingappTheme {
                ConfirmationDialog(
                    title = "Test Title",
                    message = "Test Message",
                    onDismiss = {},
                    onConfirm = { confirmCalled = true },
                    isVisible = true
                )
            }
        }

        composeTestRule
            .onNodeWithTag("confirm_button")
            .performClick()
        assert(confirmCalled)
    }
}
