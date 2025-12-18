package org.cescfe.book_publishing_app.ui.author

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
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

        composeTestRule.onNodeWithTag("create_author_top_bar").assertIsDisplayed()
        composeTestRule.onNodeWithTag("back_button").assertIsDisplayed()
        composeTestRule.onNodeWithTag("create_bottom_bar").assertIsDisplayed()
        composeTestRule.onNodeWithTag("save_button").assertIsDisplayed()
    }
}
