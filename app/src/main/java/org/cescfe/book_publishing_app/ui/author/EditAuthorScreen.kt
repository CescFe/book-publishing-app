package org.cescfe.book_publishing_app.ui.author

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.cescfe.book_publishing_app.R
import org.cescfe.book_publishing_app.ui.shared.navigation.CreateBottomBar
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAuthorScreen(authorId: String, onNavigateUp: () -> Unit = {}, onSessionExpired: () -> Unit = {}) {
    EditAuthorScreenContent(
        onNavigateUp = onNavigateUp,
        onSaveClick = {
            // TODO: Placeholder for future implementation
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EditAuthorScreenContent(onNavigateUp: () -> Unit, onSaveClick: () -> Unit) {
    Scaffold(
        modifier = Modifier.testTag("edit_author_screen"),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.edit_author_title)) },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateUp,
                        modifier = Modifier.testTag("back_button")
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_chevron_left),
                            contentDescription = stringResource(R.string.navigate_back)
                        )
                    }
                },
                modifier = Modifier.testTag("edit_author_top_bar")
            )
        },
        bottomBar = {
            CreateBottomBar(
                onSaveClick = onSaveClick,
                enabled = true
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // TODO: Form content will be added in future tickets
        }
    }
}

// ==================== PREVIEWS ====================

@Preview(showBackground = true)
@Composable
private fun EditAuthorScreenPreview() {
    BookpublishingappTheme {
        EditAuthorScreenContent(
            onNavigateUp = {},
            onSaveClick = {}
        )
    }
}
