package org.cescfe.book_publishing_app.ui.shared.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme

@Composable
fun AppBottomBar(
    modifier: Modifier = Modifier,
    selectedItem: BottomNavItem = BottomNavItem.Books,
    onItemClick: (BottomNavItem) -> Unit = {}
) {
    NavigationBar(
        modifier = modifier.testTag("app_bottom_bar")
    ) {
        BottomNavItem.items.forEach { item ->
            NavigationBarItem(
                selected = selectedItem == item,
                onClick = { onItemClick(item) },
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconResId),
                        contentDescription = stringResource(item.labelResId)
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppBottomBarPreview() {
    BookpublishingappTheme {
        AppBottomBar()
    }
}

@Preview(showBackground = true)
@Composable
private fun AppBottomBarCollectionsSelectedPreview() {
    BookpublishingappTheme {
        AppBottomBar(selectedItem = BottomNavItem.Collections)
    }
}

@Preview(showBackground = true)
@Composable
private fun AppBottomBarAuthorsSelectedPreview() {
    BookpublishingappTheme {
        AppBottomBar(selectedItem = BottomNavItem.Authors)
    }
}
