package org.cescfe.book_publishing_app.ui.shared.components

import androidx.annotation.DrawableRes
import org.cescfe.book_publishing_app.R

enum class BottomNavItem(val route: String, @field:DrawableRes val iconResId: Int, val labelResId: Int) {
    Books(
        route = "books",
        iconResId = R.drawable.book_2,
        labelResId = R.string.nav_books
    ),
    Collections(
        route = "collections",
        iconResId = R.drawable.library_books,
        labelResId = R.string.nav_collections
    ),
    Authors(
        route = "authors",
        iconResId = R.drawable.group,
        labelResId = R.string.nav_authors
    );

    companion object {
        val items = entries
    }
}