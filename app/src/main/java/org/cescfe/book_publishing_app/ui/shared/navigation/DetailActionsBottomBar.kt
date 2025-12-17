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
import org.cescfe.book_publishing_app.R
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme

@Composable
fun DetailActionsBottomBar(
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    NavigationBar(
        modifier = modifier.testTag("detail_actions_bottom_bar")
    ) {
        NavigationBarItem(
            selected = false,
            onClick = onEditClick,
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_edit),
                    contentDescription = stringResource(R.string.action_edit),
                    modifier = Modifier.testTag("edit_action_button")
                )
            }
        )
        NavigationBarItem(
            selected = false,
            onClick = onDeleteClick,
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_delete),
                    contentDescription = stringResource(R.string.action_delete),
                    modifier = Modifier.testTag("delete_action_button")
                )
            }
        )
    }
}

// ==================== PREVIEWS ====================

@Preview(showBackground = true)
@Composable
private fun DetailActionsBottomBarPreview() {
    BookpublishingappTheme {
        DetailActionsBottomBar(
            onEditClick = {},
            onDeleteClick = {}
        )
    }
}