package org.cescfe.book_publishing_app.ui.authors

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
import org.cescfe.book_publishing_app.domain.author.model.AuthorSummary
import org.cescfe.book_publishing_app.ui.authors.components.AuthorSummaryCard
import org.cescfe.book_publishing_app.ui.shared.components.AppBottomBar
import org.cescfe.book_publishing_app.ui.shared.components.BottomNavItem
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthorsScreen(
    viewModel: AuthorsViewModel = viewModel(),
    onSessionExpired: () -> Unit,
    onNavigate: (BottomNavItem) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.sessionExpired) {
        if (uiState.sessionExpired) {
            onSessionExpired()
        }
    }

    AuthorsScreenContent(
        uiState = uiState,
        onRetry = viewModel::retry,
        onNavigate = onNavigate
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AuthorsScreenContent(
    uiState: AuthorsUiState,
    onRetry: () -> Unit,
    onNavigate: (BottomNavItem) -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.testTag("authors_screen"),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.authors_title)) }
            )
        },
        bottomBar = {
            AppBottomBar(
                selectedItem = BottomNavItem.Authors,
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
                uiState.errorResId != null -> {
                    ErrorState(
                        errorMessage = stringResource(uiState.errorResId),
                        onRetry = onRetry
                    )
                }
                uiState.authorSummaries.isEmpty() -> {
                    EmptyState()
                }
                else -> {
                    AuthorsList(authorSummaries = uiState.authorSummaries)
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
            painter = painterResource(id = R.drawable.ic_person),
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.authors_empty),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun AuthorsList(authorSummaries: List<AuthorSummary>) {
    LazyColumn(
        modifier = Modifier.testTag("authors_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = authorSummaries,
            key = { author -> author.id }
        ) { author ->
            AuthorSummaryCard(authorSummary = author)
        }
    }
}

// ==================== PREVIEWS ====================

@Preview(showBackground = true)
@Composable
private fun AuthorsScreenLoadingPreview() {
    BookpublishingappTheme {
        AuthorsScreenContent(
            uiState = AuthorsUiState(isLoading = true),
            onRetry = {},
            onNavigate = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthorsScreenErrorPreview() {
    BookpublishingappTheme {
        AuthorsScreenContent(
            uiState = AuthorsUiState(errorResId = R.string.error_network),
            onRetry = {},
            onNavigate = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthorsScreenEmptyPreview() {
    BookpublishingappTheme {
        AuthorsScreenContent(
            uiState = AuthorsUiState(authorSummaries = emptyList()),
            onRetry = {},
            onNavigate = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthorsScreenSuccessPreview() {
    BookpublishingappTheme {
        AuthorsScreenContent(
            uiState = AuthorsUiState(
                authorSummaries = listOf(
                    AuthorSummary(
                        id = "1",
                        fullName = "J.R.R. Tolkien",
                        pseudonym = "Tolkien",
                        email = "tolkien@example.com"
                    ),
                    AuthorSummary(
                        id = "2",
                        fullName = "George Orwell",
                        pseudonym = null,
                        email = "orwell@example.com"
                    )
                )
            ),
            onRetry = {},
            onNavigate = {}
        )
    }
}
