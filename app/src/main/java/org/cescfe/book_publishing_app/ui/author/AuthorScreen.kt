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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.cescfe.book_publishing_app.R
import org.cescfe.book_publishing_app.ui.author.components.AuthorCard
import org.cescfe.book_publishing_app.ui.shared.components.ConfirmationDialog
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
    onNavigateUp: () -> Unit,
    onDeleteSuccess: () -> Unit
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

    LaunchedEffect(uiState.deleteSuccess) {
        if (uiState.deleteSuccess) {
            onDeleteSuccess()
        }
    }

    AuthorScreenContent(
        uiState = uiState,
        onRetry = viewModel::retry,
        onNavigateUp = onNavigateUp,
        onDeleteAuthor = viewModel::deleteAuthor
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AuthorScreenContent(
    uiState: AuthorUiState,
    onRetry: () -> Unit,
    onNavigateUp: () -> Unit,
    onDeleteAuthor: () -> Unit = {}
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

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
                onDeleteClick = { showDeleteDialog = true }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.isLoading || uiState.isDeleting -> {
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

    ConfirmationDialog(
        title = stringResource(R.string.author_delete_confirmation_title),
        message = stringResource(R.string.author_delete_confirmation_message),
        onDismiss = { showDeleteDialog = false },
        onConfirm = {
            showDeleteDialog = false
            onDeleteAuthor()
        },
        isVisible = showDeleteDialog
    )
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

@Preview(showBackground = true)
@Composable
private fun AuthorScreenDeleteDialogPreview() {
    BookpublishingappTheme {
        ConfirmationDialog(
            title = "Delete Author",
            message = "Are you sure you want to delete this author? This action cannot be undone.",
            onDismiss = {},
            onConfirm = {},
            isVisible = true
        )
    }
}
