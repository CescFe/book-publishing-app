package org.cescfe.book_publishing_app.ui.shared.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.cescfe.book_publishing_app.R
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme

@Composable
fun CreateBottomBar(modifier: Modifier = Modifier, onSaveClick: () -> Unit = {}) {
    NavigationBar(
        modifier = modifier.testTag("create_bottom_bar")
    ) {
        NavigationBarItem(
            selected = false,
            onClick = onSaveClick,
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_save),
                    contentDescription = stringResource(R.string.action_save),
                    modifier = Modifier
                        .size(29.dp)
                        .testTag("save_button")
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateBottomBarPreview() {
    BookpublishingappTheme {
        CreateBottomBar()
    }
}
