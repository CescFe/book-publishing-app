package org.cescfe.book_publishing_app.ui.book

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import org.cescfe.book_publishing_app.R
import org.cescfe.book_publishing_app.ui.shared.navigation.DetailActionsBottomBar
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookScreen(
    bookId: String,
    onSessionExpired: () -> Unit = {},
    onNavigateUp: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    BookScreenContent(
        bookId = bookId,
        onNavigateUp = onNavigateUp,
        onEditClick = onEditClick,
        onDeleteClick = onDeleteClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BookScreenContent(
    bookId: String,
    onNavigateUp: () -> Unit,
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.testTag("book_screen"),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.book_title)) },
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
                }
            )
        },
        bottomBar = {
            DetailActionsBottomBar(
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // TODO: Implement Book Details Screen
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Not Implemented yet",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// ==================== PREVIEWS ====================

@Preview(showBackground = true)
@Composable
private fun BookScreenPreview() {
    BookpublishingappTheme {
        BookScreenContent(
            bookId = "1",
            onNavigateUp = {},
            onEditClick = {},
            onDeleteClick = {}
        )
    }
}
