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
import org.cescfe.book_publishing_app.R
import org.cescfe.book_publishing_app.ui.shared.navigation.CreateBottomBar
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAuthorScreen(onNavigateUp: () -> Unit = {}) {
    Scaffold(
        modifier = Modifier.testTag("create_author_screen"),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.create_author_title)) },
                modifier = Modifier.testTag("create_author_top_bar")
            )
        },
        bottomBar = {
            CreateBottomBar(
                onSaveClick = {
                    // TODO: Placeholder for future implementation
                }
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

@Preview(showBackground = true)
@Composable
private fun CreateAuthorScreenPreview() {
    BookpublishingappTheme {
        CreateAuthorScreen()
    }
}
