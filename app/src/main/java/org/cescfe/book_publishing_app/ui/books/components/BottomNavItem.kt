package org.cescfe.book_publishing_app.ui.books.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import org.cescfe.book_publishing_app.R

enum class BottomNavItem(val route: String, val labelResId: Int) {
    Books(
        route = "books",
        labelResId = R.string.nav_books
    ),
    Collections(
        route = "collections",
        labelResId = R.string.nav_collections
    ),
    Authors(
        route = "authors",
        labelResId = R.string.nav_authors
    );

    val icon: ImageVector
        @Composable
        get() = when (this) {
            Books -> Icons.AutoMirrored.Filled.MenuBook
            Collections -> Icons.Default.Folder
            Authors -> Icons.Default.Person
        }

    companion object {
        val items = entries
    }
}
