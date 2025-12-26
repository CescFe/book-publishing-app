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
import org.cescfe.book_publishing_app.ui.author.UpdateAuthorScreen
import org.cescfe.book_publishing_app.ui.book.BookScreen
import org.cescfe.book_publishing_app.ui.book.BooksScreen
import org.cescfe.book_publishing_app.ui.book.CreateBookScreen
import org.cescfe.book_publishing_app.ui.collection.CollectionsScreen
import org.cescfe.book_publishing_app.ui.shared.navigation.BottomNavItem
import org.cescfe.book_publishing_app.ui.splash.SplashScreen

object Routes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val BOOKS = "books"
    const val BOOK = "book"
    const val CREATE_BOOK = "create_book"
    const val COLLECTIONS = "collections"
    const val AUTHORS = "authors"
    const val AUTHOR = "author"
    const val CREATE_AUTHOR = "create_author"
    const val UPDATE_AUTHOR = "update_author"

    fun author(authorId: String) = "author/$authorId"
    fun updateAuthor(authorId: String) = "update_author/$authorId"
    fun book(bookId: String) = "book/$bookId"
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
                },
                onBookClick = { bookId ->
                    navController.navigate(Routes.book(bookId))
                },
                onCreateBookClick = {
                    navController.navigate(Routes.CREATE_BOOK)
                }
            )
        }
        composable(
            route = "${Routes.BOOK}/{bookId}",
            arguments = listOf(
                navArgument("bookId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
            BookScreen(
                bookId = bookId,
                onSessionExpired = {
                    TokenManager.clearToken()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.BOOKS) { inclusive = true }
                    }
                },
                onNavigateUp = {
                    navController.navigateUp()
                },
                onEditClick = {
                    // TODO: Placeholder for future implementation
                },
                onDeleteClick = {
                    // TODO: Placeholder for future implementation
                }
            )
        }
        composable(Routes.CREATE_BOOK) {
            CreateBookScreen(
                onNavigateUp = { navController.navigateUp() },
                onSessionExpired = {
                    TokenManager.clearToken()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.BOOKS) { inclusive = true }
                    }
                },
                onBookCreated = { bookId ->
                    navController.navigate(Routes.book(bookId)) {
                        popUpTo(Routes.BOOKS) { inclusive = false }
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
        composable(Routes.AUTHORS) { backStackEntry ->
            val shouldRefresh = backStackEntry.savedStateHandle.get<Boolean>("refresh") ?: false

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
                },
                shouldRefresh = shouldRefresh,
                onRefreshHandled = {
                    backStackEntry.savedStateHandle["refresh"] = false
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
                },
                onEditClick = {
                    navController.navigate(Routes.updateAuthor(authorId))
                }
            )
        }
        composable(Routes.CREATE_AUTHOR) {
            CreateAuthorScreen(
                onNavigateUp = { navController.navigateUp() },
                onSessionExpired = {
                    TokenManager.clearToken()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.BOOKS) { inclusive = true }
                    }
                },
                onAuthorCreated = { authorId ->
                    navController.navigate(Routes.author(authorId)) {
                        popUpTo(Routes.AUTHORS) { inclusive = false }
                    }
                    navController.getBackStackEntry(Routes.AUTHORS)
                        .savedStateHandle["refresh"] = true
                }
            )
        }
        composable(
            route = "${Routes.UPDATE_AUTHOR}/{authorId}",
            arguments = listOf(
                navArgument("authorId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val authorId = backStackEntry.arguments?.getString("authorId") ?: ""
            UpdateAuthorScreen(
                authorId = authorId,
                onNavigateUp = { navController.navigateUp() },
                onSessionExpired = {
                    TokenManager.clearToken()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.BOOKS) { inclusive = true }
                    }
                },
                onAuthorUpdated = { updatedAuthorId ->
                    navController.navigate(Routes.author(updatedAuthorId)) {
                        popUpTo(Routes.AUTHORS) { inclusive = false }
                    }
                    navController.getBackStackEntry(Routes.AUTHORS)
                        .savedStateHandle["refresh"] = true
                }
            )
        }
    }
}
