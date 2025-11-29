package org.cescfe.book_publishing_app.ui.navigation

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class AppNavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun appNavigation_startDestination_isSplash() {
        lateinit var navController: TestNavHostController

        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            AppNavigation(navController = navController)
        }

        composeTestRule.waitForIdle()
        assertEquals(Routes.SPLASH, navController.currentBackStackEntry?.destination?.route)
    }
}
