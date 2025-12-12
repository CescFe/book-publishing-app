package org.cescfe.book_publishing_app.ui.books

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.cescfe.book_publishing_app.R
import org.cescfe.book_publishing_app.domain.book.model.BookSummary
import org.cescfe.book_publishing_app.ui.books.components.BookSummaryCard
import org.cescfe.book_publishing_app.ui.shared.components.AppBottomBar
import org.cescfe.book_publishing_app.ui.shared.components.BottomNavItem
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BooksScreen(
    viewModel: BooksViewModel = viewModel(),
    onSessionExpired: () -> Unit,
    onNavigate: (BottomNavItem) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.sessionExpired) {
        if (uiState.sessionExpired) {
            onSessionExpired()
        }
    }

    BooksScreenContent(
        uiState = uiState,
        onRetry = viewModel::retry,
        onNavigate = onNavigate
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BooksScreenContent(uiState: BooksUiState, onRetry: () -> Unit, onNavigate: (BottomNavItem) -> Unit = {}) {
    Scaffold(
        modifier = Modifier.testTag("books_screen"),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.books_title)) }
            )
        },
        bottomBar = {
            AppBottomBar(
                selectedItem = BottomNavItem.Books,
                onItemClick = onNavigate
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.isLoading -> {
                    LoadingState()
                }
                uiState.error != null -> {
                    ErrorState(
                        errorMessage = uiState.error,
                        onRetry = onRetry
                    )
                }
                uiState.bookSummaries.isEmpty() -> {
                    EmptyState()
                }
                else -> {
                    BooksList(bookSummaries = uiState.bookSummaries)
                }
            }
        }
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("loading_indicator"),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorState(errorMessage: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("error_state"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .testTag("error_message")
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onRetry,
            modifier = Modifier.testTag("retry_button")
        ) {
            Text(text = stringResource(R.string.books_retry))
        }
    }
}

@Composable
private fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("empty_state"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.book_2),
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.books_empty),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun BooksList(bookSummaries: List<BookSummary>) {
    LazyColumn(
        modifier = Modifier.testTag("books_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = bookSummaries,
            key = { book -> book.id }
        ) { book ->
            BookSummaryCard(bookSummary = book)
        }
    }
}

// ==================== PREVIEWS ====================

@Preview(showBackground = true)
@Composable
private fun BooksScreenLoadingPreview() {
    BookpublishingappTheme {
        BooksScreenContent(
            uiState = BooksUiState(isLoading = true),
            onRetry = {},
            onNavigate = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BooksScreenErrorPreview() {
    BookpublishingappTheme {
        BooksScreenContent(
            uiState = BooksUiState(error = "Network error. Please check your connection."),
            onRetry = {},
            onNavigate = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BooksScreenEmptyPreview() {
    BookpublishingappTheme {
        BooksScreenContent(
            uiState = BooksUiState(bookSummaries = emptyList()),
            onRetry = {},
            onNavigate = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BooksScreenSuccessPreview() {
    BookpublishingappTheme {
        BooksScreenContent(
            uiState = BooksUiState(
                bookSummaries = listOf(
                    BookSummary(
                        id = "1",
                        title = "The Lord of the Rings",
                        author = "J.R.R. Tolkien",
                        collection = "Fantasy",
                        finalPrice = 29.99,
                        isbn = "978-0-544-00001-0"
                    ),
                    BookSummary(
                        id = "2",
                        title = "1984",
                        author = "George Orwell",
                        collection = "Classics",
                        finalPrice = 15.50,
                        isbn = null
                    )
                )
            ),
            onRetry = {},
            onNavigate = {}
        )
    }
}
