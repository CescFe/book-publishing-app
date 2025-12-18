package org.cescfe.book_publishing_app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.cescfe.book_publishing_app.data.auth.TokenManager
import org.cescfe.book_publishing_app.ui.auth.LoginScreen
import org.cescfe.book_publishing_app.ui.author.AuthorScreen
import org.cescfe.book_publishing_app.ui.author.AuthorsScreen
import org.cescfe.book_publishing_app.ui.author.CreateAuthorScreen
import org.cescfe.book_publishing_app.ui.book.BooksScreen
import org.cescfe.book_publishing_app.ui.collection.CollectionsScreen
import org.cescfe.book_publishing_app.ui.shared.navigation.BottomNavItem
import org.cescfe.book_publishing_app.ui.splash.SplashScreen

object Routes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val BOOKS = "books"
    const val COLLECTIONS = "collections"
    const val AUTHORS = "authors"
    const val AUTHOR = "author"
    const val CREATE_AUTHOR = "create_author"

    fun author(authorId: String) = "author/$authorId"
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {
        composable(Routes.SPLASH) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.BOOKS) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.BOOKS) {
            BooksScreen(
                onSessionExpired = {
                    TokenManager.clearToken()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.BOOKS) { inclusive = true }
                    }
                },
                onNavigate = { item ->
                    if (item.route != Routes.BOOKS) {
                        navController.navigate(item.route) {
                            popUpTo(Routes.BOOKS) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
        composable(Routes.COLLECTIONS) {
            CollectionsScreen(
                onSessionExpired = {
                    TokenManager.clearToken()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.BOOKS) { inclusive = true }
                    }
                },
                onNavigate = { item ->
                    if (item.route != Routes.COLLECTIONS) {
                        navController.navigate(item.route) {
                            popUpTo(Routes.BOOKS) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
        composable(Routes.AUTHORS) {
            AuthorsScreen(
                onSessionExpired = {
                    TokenManager.clearToken()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.BOOKS) { inclusive = true }
                    }
                },
                onNavigate = { item ->
                    if (item.route != Routes.AUTHORS) {
                        navController.navigate(item.route) {
                            popUpTo(Routes.BOOKS) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                onAuthorClick = { authorId ->
                    navController.navigate(Routes.author(authorId))
                },
                onCreateAuthorClick = {
                    navController.navigate(Routes.CREATE_AUTHOR)
                }
            )
        }
        composable(
            route = "${Routes.AUTHOR}/{authorId}",
            arguments = listOf(
                navArgument("authorId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val authorId = backStackEntry.arguments?.getString("authorId") ?: ""
            AuthorScreen(
                authorId = authorId,
                onSessionExpired = {
                    TokenManager.clearToken()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.BOOKS) { inclusive = true }
                    }
                },
                onNavigateUp = {
                    navController.navigateUp()
                },
                onDeleteSuccess = {
                    navController.navigate(BottomNavItem.Authors.route) {
                        popUpTo(BottomNavItem.Authors.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.CREATE_AUTHOR) {
            CreateAuthorScreen(
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}
