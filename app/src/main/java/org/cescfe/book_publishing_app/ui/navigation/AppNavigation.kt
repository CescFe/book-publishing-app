package org.cescfe.book_publishing_app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.cescfe.book_publishing_app.data.auth.TokenManager
import org.cescfe.book_publishing_app.ui.auth.LoginScreen
import org.cescfe.book_publishing_app.ui.author.AuthorsScreen
import org.cescfe.book_publishing_app.ui.book.BooksScreen
import org.cescfe.book_publishing_app.ui.collection.CollectionsScreen
import org.cescfe.book_publishing_app.ui.splash.SplashScreen

object Routes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val BOOKS = "books"
    const val COLLECTIONS = "collections"
    const val AUTHORS = "authors"
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
                }
            )
        }
    }
}
