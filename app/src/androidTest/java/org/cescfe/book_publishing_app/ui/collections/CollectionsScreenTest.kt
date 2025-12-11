package org.cescfe.book_publishing_app.ui.collections

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

class CollectionsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // ==================== SMOKE TEST ====================

    @Test
    fun collectionsScreen_rendersWithoutCrash() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                CollectionsScreen()
            }
        }

        composeTestRule.onNodeWithTag("collections_screen").assertIsDisplayed()
    }

    // ==================== TOP BAR ====================

    @Test
    fun collectionsScreen_showsTitle() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                CollectionsScreen()
            }
        }

        composeTestRule.onNodeWithText("Collections").assertIsDisplayed()
    }

    // ==================== BOTTOM NAVIGATION ====================

    @Test
    fun collectionsScreen_showsBottomNavigationBar() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                CollectionsScreen()
            }
        }

        composeTestRule.onNodeWithTag("app_bottom_bar").assertIsDisplayed()
    }

    @Test
    fun collectionsScreen_bottomBar_callsOnNavigate_whenBooksClicked() {
        var navigatedItem: BottomNavItem? = null

        composeTestRule.setContent {
            BookpublishingappTheme {
                CollectionsScreen(
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
    fun collectionsScreen_bottomBar_callsOnNavigate_whenAuthorsClicked() {
        var navigatedItem: BottomNavItem? = null

        composeTestRule.setContent {
            BookpublishingappTheme {
                CollectionsScreen(
                    onNavigate = { item -> navigatedItem = item }
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Authors").performClick()

        assert(navigatedItem == BottomNavItem.Authors) {
            "Should navigate to Authors, but got $navigatedItem"
        }
    }
}
