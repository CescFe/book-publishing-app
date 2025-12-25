package org.cescfe.book_publishing_app.ui.book

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.lifecycle.viewmodel.compose.viewModel
import org.cescfe.book_publishing_app.R
import org.cescfe.book_publishing_app.ui.shared.components.ConfirmationDialog
import org.cescfe.book_publishing_app.ui.shared.components.LoadingState
import org.cescfe.book_publishing_app.ui.shared.navigation.CreateBottomBar
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBookScreen(
    viewModel: CreateBookViewModel = viewModel(),
    onNavigateUp: () -> Unit = {},
    onSessionExpired: () -> Unit = {},
    onBookCreated: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.createdBookId) {
        uiState.createdBookId?.let { bookId ->
            onBookCreated(bookId)
        }
    }

    LaunchedEffect(uiState.sessionExpired) {
        if (uiState.sessionExpired) {
            onSessionExpired()
            viewModel.onSessionExpiredHandled()
        }
    }

    CreateBookScreenContent(
        uiState = uiState,
        onNavigateUp = onNavigateUp,
        onSaveClicked = viewModel::onSaveClicked,
        onDismissDialog = viewModel::dismissConfirmDialog,
        onConfirmCreateBook = viewModel::createBook
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CreateBookScreenContent(
    uiState: CreateBookUiState,
    onNavigateUp: () -> Unit,
    onSaveClicked: () -> Unit,
    onDismissDialog: () -> Unit,
    onConfirmCreateBook: () -> Unit
) {
    Scaffold(
        modifier = Modifier.testTag("create_book_screen"),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.create_book_title)) },
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
                },
                modifier = Modifier.testTag("create_book_top_bar")
            )
        },
        bottomBar = {
            CreateBottomBar(
                onSaveClick = onSaveClicked,
                enabled = !uiState.isLoading
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
                else -> {
                    // TODO: Add form fields to create a book
                }
            }
        }
    }

    ConfirmationDialog(
        title = stringResource(R.string.create_book_confirmation_title),
        message = stringResource(R.string.create_book_confirmation_message),
        isVisible = uiState.showConfirmDialog,
        onDismiss = onDismissDialog,
        onConfirm = onConfirmCreateBook
    )
}

// ==================== PREVIEWS ====================

@Preview(showBackground = true)
@Composable
private fun CreateBookScreenEmptyPreview() {
    BookpublishingappTheme {
        CreateBookScreenContent(
            uiState = CreateBookUiState(),
            onNavigateUp = {},
            onSaveClicked = {},
            onDismissDialog = {},
            onConfirmCreateBook = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateBookScreenLoadingPreview() {
    BookpublishingappTheme {
        CreateBookScreenContent(
            uiState = CreateBookUiState(isLoading = true),
            onNavigateUp = {},
            onSaveClicked = {},
            onDismissDialog = {},
            onConfirmCreateBook = {}
        )
    }
}
