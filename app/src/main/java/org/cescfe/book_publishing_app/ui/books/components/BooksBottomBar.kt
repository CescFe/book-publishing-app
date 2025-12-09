package org.cescfe.book_publishing_app.ui.books.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme

@Composable
fun BooksBottomBar(
    modifier: Modifier = Modifier,
    selectedItem: BottomNavItem = BottomNavItem.Books,
    onItemClick: (BottomNavItem) -> Unit = {}
) {
    NavigationBar(modifier = modifier) {
        BottomNavItem.items.forEach { item ->
            NavigationBarItem(
                selected = selectedItem == item,
                onClick = { onItemClick(item) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = stringResource(item.labelResId)
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BooksBottomBarPreview() {
    BookpublishingappTheme {
        BooksBottomBar(
            selectedItem = BottomNavItem.Books,
            onItemClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BooksBottomBarCollectionsSelectedPreview() {
    BookpublishingappTheme {
        BooksBottomBar(
            selectedItem = BottomNavItem.Collections,
            onItemClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BooksBottomBarAuthorsSelectedPreview() {
    BookpublishingappTheme {
        BooksBottomBar(
            selectedItem = BottomNavItem.Authors,
            onItemClick = {}
        )
    }
}
