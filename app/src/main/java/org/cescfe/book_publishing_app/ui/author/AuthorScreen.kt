package org.cescfe.book_publishing_app.ui.author

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import org.cescfe.book_publishing_app.ui.author.components.AuthorCard
import org.cescfe.book_publishing_app.ui.shared.components.ErrorState
import org.cescfe.book_publishing_app.ui.shared.components.LoadingState
import org.cescfe.book_publishing_app.ui.shared.navigation.DetailActionsBottomBar
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthorScreen(
    authorId: String,
    viewModel: AuthorViewModel = viewModel(),
    onSessionExpired: () -> Unit,
    onNavigateUp: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(authorId) {
        viewModel.loadAuthor(authorId)
    }

    LaunchedEffect(uiState.sessionExpired) {
        if (uiState.sessionExpired) {
            onSessionExpired()
        }
    }

    AuthorScreenContent(
        uiState = uiState,
        onRetry = viewModel::retry,
        onNavigateUp = onNavigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AuthorScreenContent(uiState: AuthorUiState, onRetry: () -> Unit, onNavigateUp: () -> Unit) {
    Scaffold(
        modifier = Modifier.testTag("author_screen"),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.author_title)) },
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
                onEditClick = {
                    // TODO: Placeholder for future implementation
                },
                onDeleteClick = {
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
                uiState.author != null -> {
                    AuthorCard(
                        author = uiState.author,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState())
                    )
                }
            }
        }
    }
}

// ==================== PREVIEWS ====================

@Preview(showBackground = true)
@Composable
private fun AuthorScreenLoadingPreview() {
    BookpublishingappTheme {
        AuthorScreenContent(
            uiState = AuthorUiState(isLoading = true),
            onRetry = {},
            onNavigateUp = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthorScreenErrorPreview() {
    BookpublishingappTheme {
        AuthorScreenContent(
            uiState = AuthorUiState(errorResId = R.string.error_network),
            onRetry = {},
            onNavigateUp = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthorScreenSuccessPreview() {
    BookpublishingappTheme {
        AuthorScreenContent(
            uiState = AuthorUiState(
                author = org.cescfe.book_publishing_app.domain.author.model.Author(
                    id = "1",
                    fullName = "J.R.R. Tolkien",
                    pseudonym = "Tolkien",
                    biography = "John Ronald Reuel Tolkien was an English writer and philologist. " +
                        "He was the author of the high fantasy works The Hobbit and The Lord of the Rings.",
                    email = "tolkien@example.com",
                    website = "https://www.tolkienestate.com"
                )
            ),
            onRetry = {},
            onNavigateUp = {}
        )
    }
}
