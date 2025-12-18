package org.cescfe.book_publishing_app.ui.author

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
import org.cescfe.book_publishing_app.domain.author.model.AuthorSummary
import org.cescfe.book_publishing_app.ui.author.components.AuthorSummaryCard
import org.cescfe.book_publishing_app.ui.shared.components.EmptyState
import org.cescfe.book_publishing_app.ui.shared.components.ErrorState
import org.cescfe.book_publishing_app.ui.shared.components.LoadingState
import org.cescfe.book_publishing_app.ui.shared.navigation.AppBottomBar
import org.cescfe.book_publishing_app.ui.shared.navigation.BottomNavItem
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthorsScreen(
    viewModel: AuthorsViewModel = viewModel(),
    onSessionExpired: () -> Unit,
    onNavigate: (BottomNavItem) -> Unit = {},
    onAuthorClick: (String) -> Unit = {},
    onCreateAuthorClick: () -> Unit = {},
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
        }
    }

    AuthorsScreenContent(
        uiState = uiState,
        onRetry = viewModel::retry,
        onNavigate = onNavigate,
        onAuthorClick = onAuthorClick,
        onCreateAuthorClick = onCreateAuthorClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AuthorsScreenContent(
    uiState: AuthorsUiState,
    onRetry: () -> Unit,
    onNavigate: (BottomNavItem) -> Unit = {},
    onAuthorClick: (String) -> Unit = {},
    onCreateAuthorClick: () -> Unit = {}
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateAuthorClick,
                shape = CircleShape,
                modifier = Modifier.testTag("create_author_fab")
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = stringResource(R.string.fab_create_author)
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
                uiState.authorSummaries.isEmpty() -> {
                    EmptyState(
                        iconRes = R.drawable.ic_person,
                        messageRes = R.string.authors_empty
                    )
                }
                else -> {
                    AuthorsList(
                        authorSummaries = uiState.authorSummaries,
                        onAuthorClick = onAuthorClick
                    )
                }
            }
        }
    }
}

@Composable
private fun AuthorsList(authorSummaries: List<AuthorSummary>, onAuthorClick: (String) -> Unit = {}) {
    LazyColumn(
        modifier = Modifier.testTag("authors_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = authorSummaries,
            key = { author -> author.id }
        ) { author ->
            AuthorSummaryCard(
                authorSummary = author,
                onClick = { onAuthorClick(author.id) }
            )
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
