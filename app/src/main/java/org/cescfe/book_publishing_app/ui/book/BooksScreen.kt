package org.cescfe.book_publishing_app.ui.book

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.cescfe.book_publishing_app.R
import org.cescfe.book_publishing_app.domain.book.model.BookSummary
import org.cescfe.book_publishing_app.ui.book.components.BookSummaryCard
import org.cescfe.book_publishing_app.ui.shared.components.EmptyState
import org.cescfe.book_publishing_app.ui.shared.components.ErrorState
import org.cescfe.book_publishing_app.ui.shared.components.LoadingState
import org.cescfe.book_publishing_app.ui.shared.navigation.AppBottomBar
import org.cescfe.book_publishing_app.ui.shared.navigation.BottomNavItem
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BooksScreen(
    viewModel: BooksViewModel = viewModel(),
    onSessionExpired: () -> Unit,
    onNavigate: (BottomNavItem) -> Unit = {},
    onBookClick: (String) -> Unit = {},
    onCreateBookClick: () -> Unit = {},
    shouldRefresh: Boolean = false,
    onRefreshHandled: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(shouldRefresh) {
        if (shouldRefresh) {
            viewModel.retry()
            onRefreshHandled()
        }
    }

    LaunchedEffect(uiState.sessionExpired) {
        if (uiState.sessionExpired) {
            onSessionExpired()
            viewModel.onSessionExpiredHandled()
        }
    }

    BooksScreenContent(
        uiState = uiState,
        onRetry = viewModel::retry,
        onNavigate = onNavigate,
        onBookClick = onBookClick,
        onCreateBookClick = onCreateBookClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BooksScreenContent(
    uiState: BooksUiState,
    onRetry: () -> Unit,
    onNavigate: (BottomNavItem) -> Unit = {},
    onBookClick: (String) -> Unit = {},
    onCreateBookClick: () -> Unit = {}
) {
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateBookClick,
                shape = CircleShape,
                modifier = Modifier.testTag("create_book_fab")
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = stringResource(R.string.fab_create_book)
                )
            }
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
                uiState.errorResId != null -> {
                    ErrorState(
                        errorMessage = stringResource(uiState.errorResId),
                        onRetry = onRetry
                    )
                }
                uiState.bookSummaries.isEmpty() -> {
                    EmptyState(
                        iconRes = R.drawable.book_2,
                        messageRes = R.string.books_empty
                    )
                }
                else -> {
                    BooksList(
                        bookSummaries = uiState.bookSummaries,
                        onBookClick = onBookClick
                    )
                }
            }
        }
    }
}

@Composable
private fun BooksList(bookSummaries: List<BookSummary>, onBookClick: (String) -> Unit = {}) {
    LazyColumn(
        modifier = Modifier.testTag("books_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = bookSummaries,
            key = { book -> book.id }
        ) { book ->
            BookSummaryCard(
                bookSummary = book,
                onClick = { onBookClick(book.id) }
            )
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
            uiState = BooksUiState(errorResId = R.string.error_network),
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
