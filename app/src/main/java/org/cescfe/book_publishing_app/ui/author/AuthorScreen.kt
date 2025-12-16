package org.cescfe.book_publishing_app.ui.author

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.cescfe.book_publishing_app.R
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthorScreen(authorId: String, onSessionExpired: () -> Unit, onNavigateUp: () -> Unit) {
    Scaffold(
        modifier = Modifier.testTag("author_screen"),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.author_title)) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Author ID: $authorId",
                modifier = Modifier.testTag("author_id_text")
            )
        }
    }
}

// ==================== PREVIEWS ====================

@Preview(showBackground = true)
@Composable
private fun AuthorScreenPreview() {
    BookpublishingappTheme {
        AuthorScreen(
            authorId = "1",
            onSessionExpired = {},
            onNavigateUp = {}
        )
    }
}
