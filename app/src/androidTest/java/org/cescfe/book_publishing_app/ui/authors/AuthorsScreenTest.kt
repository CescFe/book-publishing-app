package org.cescfe.book_publishing_app.ui.authors

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.cescfe.book_publishing_app.ui.shared.components.BottomNavItem
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme
import org.junit.Rule
import org.junit.Test

class AuthorsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // ==================== SMOKE TEST ====================

    @Test
    fun authorsScreen_rendersWithoutCrash() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                AuthorsScreen()
            }
        }

        composeTestRule.onNodeWithTag("authors_screen").assertIsDisplayed()
    }

    // ==================== TOP BAR ====================

    @Test
    fun authorsScreen_showsTitle() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                AuthorsScreen()
            }
        }

        composeTestRule.onNodeWithText("Authors").assertIsDisplayed()
    }

    // ==================== BOTTOM NAVIGATION ====================

    @Test
    fun authorsScreen_showsBottomNavigationBar() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                AuthorsScreen()
            }
        }

        composeTestRule.onNodeWithTag("app_bottom_bar").assertIsDisplayed()
    }

    @Test
    fun authorsScreen_bottomBar_callsOnNavigate_whenBooksClicked() {
        var navigatedItem: BottomNavItem? = null

        composeTestRule.setContent {
            BookpublishingappTheme {
                AuthorsScreen(
                    onNavigate = { item -> navigatedItem = item }
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Books").performClick()

        assert(navigatedItem == BottomNavItem.Books) {
            "Should navigate to Books, but got $navigatedItem"
        }
    }

    @Test
    fun authorsScreen_bottomBar_callsOnNavigate_whenCollectionsClicked() {
        var navigatedItem: BottomNavItem? = null

        composeTestRule.setContent {
            BookpublishingappTheme {
                AuthorsScreen(
                    onNavigate = { item -> navigatedItem = item }
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Collections").performClick()

        assert(navigatedItem == BottomNavItem.Collections) {
            "Should navigate to Collections, but got $navigatedItem"
        }
    }
}
