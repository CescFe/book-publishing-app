package org.cescfe.book_publishing_app.ui.collections

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
import org.cescfe.book_publishing_app.domain.collection.model.CollectionSummary
import org.cescfe.book_publishing_app.ui.collections.components.CollectionSummaryCard
import org.cescfe.book_publishing_app.ui.shared.components.AppBottomBar
import org.cescfe.book_publishing_app.ui.shared.components.BottomNavItem
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionsScreen(
    viewModel: CollectionsViewModel = viewModel(),
    onSessionExpired: () -> Unit,
    onNavigate: (BottomNavItem) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.sessionExpired) {
        if (uiState.sessionExpired) {
            onSessionExpired()
        }
    }

    CollectionsScreenContent(
        uiState = uiState,
        onRetry = viewModel::retry,
        onNavigate = onNavigate
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CollectionsScreenContent(
    uiState: CollectionsUiState,
    onRetry: () -> Unit,
    onNavigate: (BottomNavItem) -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.testTag("collections_screen"),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.collections_title)) }
            )
        },
        bottomBar = {
            AppBottomBar(
                selectedItem = BottomNavItem.Collections,
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
                uiState.collectionSummaries.isEmpty() -> {
                    EmptyState()
                }
                else -> {
                    CollectionsList(collectionSummaries = uiState.collectionSummaries)
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
            painter = painterResource(id = R.drawable.library_books),
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.collections_empty),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun CollectionsList(collectionSummaries: List<CollectionSummary>) {
    LazyColumn(
        modifier = Modifier.testTag("collections_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = collectionSummaries,
            key = { collection -> collection.id }
        ) { collection ->
            CollectionSummaryCard(collectionSummary = collection)
        }
    }
}

// ==================== PREVIEWS ====================

@Preview(showBackground = true)
@Composable
private fun CollectionsScreenLoadingPreview() {
    BookpublishingappTheme {
        CollectionsScreenContent(
            uiState = CollectionsUiState(isLoading = true),
            onRetry = {},
            onNavigate = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CollectionsScreenErrorPreview() {
    BookpublishingappTheme {
        CollectionsScreenContent(
            uiState = CollectionsUiState(errorResId = R.string.error_network),
            onRetry = {},
            onNavigate = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CollectionsScreenEmptyPreview() {
    BookpublishingappTheme {
        CollectionsScreenContent(
            uiState = CollectionsUiState(collectionSummaries = emptyList()),
            onRetry = {},
            onNavigate = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CollectionsScreenSuccessPreview() {
    BookpublishingappTheme {
        CollectionsScreenContent(
            uiState = CollectionsUiState(
                collectionSummaries = listOf(
                    CollectionSummary(
                        id = "1",
                        name = "Fantasy Collection",
                        readingLevel = "Advanced",
                        primaryLanguage = "English",
                        primaryGenre = "Fantasy"
                    ),
                    CollectionSummary(
                        id = "2",
                        name = "Classic Literature",
                        readingLevel = null,
                        primaryLanguage = "Spanish",
                        primaryGenre = "Classics"
                    )
                )
            ),
            onRetry = {},
            onNavigate = {}
        )
    }
}
