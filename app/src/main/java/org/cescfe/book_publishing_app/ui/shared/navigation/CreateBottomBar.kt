package org.cescfe.book_publishing_app.ui.shared.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.cescfe.book_publishing_app.R
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme

@Composable
fun CreateBottomBar(
    modifier: Modifier = Modifier,
    onSaveClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier.testTag("create_bottom_bar"),
        tonalElevation = 3.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = onSaveClick,
                modifier = Modifier.testTag("save_button")
            ) {
                Text(text = stringResource(R.string.action_save))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateBottomBarPreview() {
    BookpublishingappTheme {
        CreateBottomBar()
    }
}
