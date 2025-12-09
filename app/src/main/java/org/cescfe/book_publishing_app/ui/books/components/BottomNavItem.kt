package org.cescfe.book_publishing_app.ui.books.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import org.cescfe.book_publishing_app.R

sealed class BottomNavItem(val route: String, val icon: ImageVector, val labelResId: Int) {
    data object Books : BottomNavItem(
        route = "books",
        icon = Icons.AutoMirrored.Filled.MenuBook,
        labelResId = R.string.nav_books
    )

    data object Collections : BottomNavItem(
        route = "collections",
        icon = Icons.Default.Folder,
        labelResId = R.string.nav_collections
    )

    data object Authors : BottomNavItem(
        route = "authors",
        icon = Icons.Default.Person,
        labelResId = R.string.nav_authors
    )

    companion object {
        val items = listOf(Books, Collections, Authors)
    }
}
