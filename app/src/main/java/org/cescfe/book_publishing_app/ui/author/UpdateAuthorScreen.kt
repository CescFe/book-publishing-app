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
import org.cescfe.book_publishing_app.ui.author.components.AuthorFormFields
import org.cescfe.book_publishing_app.ui.shared.components.ConfirmationDialog
import org.cescfe.book_publishing_app.ui.shared.components.LoadingState
import org.cescfe.book_publishing_app.ui.shared.navigation.CreateBottomBar
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateAuthorScreen(
    authorId: String,
    viewModel: UpdateAuthorViewModel = viewModel(),
    onNavigateUp: () -> Unit = {},
    onSessionExpired: () -> Unit = {},
    onAuthorUpdated: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(authorId) {
        viewModel.loadAuthor(authorId)
    }

    LaunchedEffect(uiState.updatedAuthorId) {
        uiState.updatedAuthorId?.let { updatedAuthorId ->
            onAuthorUpdated(updatedAuthorId)
        }
    }

    LaunchedEffect(uiState.sessionExpired) {
        if (uiState.sessionExpired) {
            onSessionExpired()
            viewModel.onSessionExpiredHandled()
        }
    }

    UpdateAuthorScreenContent(
        uiState = uiState,
        onNavigateUp = onNavigateUp,
        onFullNameChange = viewModel::onFullNameChange,
        onPseudonymChange = viewModel::onPseudonymChange,
        onBiographyChange = viewModel::onBiographyChange,
        onEmailChange = viewModel::onEmailChange,
        onWebsiteChange = viewModel::onWebsiteChange,
        onSaveClicked = viewModel::onSaveClicked,
        onDismissDialog = viewModel::dismissConfirmDialog,
        onConfirmUpdateAuthor = viewModel::updateAuthor
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun UpdateAuthorScreenContent(
    uiState: UpdateAuthorUiState,
    onNavigateUp: () -> Unit,
    onFullNameChange: (String) -> Unit,
    onPseudonymChange: (String) -> Unit,
    onBiographyChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onWebsiteChange: (String) -> Unit,
    onSaveClicked: () -> Unit,
    onDismissDialog: () -> Unit,
    onConfirmUpdateAuthor: () -> Unit
) {
    Scaffold(
        modifier = Modifier.testTag("update_author_screen"),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.update_author_title)) },
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
                modifier = Modifier.testTag("update_author_top_bar")
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
                uiState.errorResId != null -> {
                    // Error state - could reuse ErrorState component if needed
                    // For now, just show loading or empty
                }
                else -> {
                    // Reuse the same AuthorFormFields component
                    AuthorFormFields(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        fullName = uiState.fullName,
                        onFullNameChange = onFullNameChange,
                        pseudonym = uiState.pseudonym,
                        onPseudonymChange = onPseudonymChange,
                        biography = uiState.biography,
                        onBiographyChange = onBiographyChange,
                        email = uiState.email,
                        onEmailChange = onEmailChange,
                        website = uiState.website,
                        onWebsiteChange = onWebsiteChange,
                        fullNameError = uiState.fullNameError?.let { stringResource(it) },
                        pseudonymError = uiState.pseudonymError?.let { stringResource(it) },
                        biographyError = uiState.biographyError?.let { stringResource(it) },
                        emailError = uiState.emailError?.let { stringResource(it) },
                        websiteError = uiState.websiteError?.let { stringResource(it) }
                    )
                }
            }
        }
    }

    ConfirmationDialog(
        title = stringResource(R.string.update_author_confirmation_title),
        message = stringResource(R.string.update_author_confirmation_message),
        isVisible = uiState.showConfirmDialog,
        onDismiss = onDismissDialog,
        onConfirm = onConfirmUpdateAuthor
    )
}

// ==================== PREVIEWS ====================

@Preview(showBackground = true)
@Composable
private fun UpdateAuthorScreenEmptyPreview() {
    BookpublishingappTheme {
        UpdateAuthorScreenContent(
            uiState = UpdateAuthorUiState(),
            onNavigateUp = {},
            onFullNameChange = {},
            onPseudonymChange = {},
            onBiographyChange = {},
            onEmailChange = {},
            onWebsiteChange = {},
            onSaveClicked = {},
            onDismissDialog = {},
            onConfirmUpdateAuthor = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UpdateAuthorScreenWithDataPreview() {
    BookpublishingappTheme {
        UpdateAuthorScreenContent(
            uiState = UpdateAuthorUiState(
                fullName = "J.R.R. Tolkien",
                pseudonym = "Tolkien",
                biography = "English writer",
                email = "tolkien@example.com",
                website = "https://tolkien.com"
            ),
            onNavigateUp = {},
            onFullNameChange = {},
            onPseudonymChange = {},
            onBiographyChange = {},
            onEmailChange = {},
            onWebsiteChange = {},
            onSaveClicked = {},
            onDismissDialog = {},
            onConfirmUpdateAuthor = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UpdateAuthorScreenLoadingPreview() {
    BookpublishingappTheme {
        UpdateAuthorScreenContent(
            uiState = UpdateAuthorUiState(isLoading = true),
            onNavigateUp = {},
            onFullNameChange = {},
            onPseudonymChange = {},
            onBiographyChange = {},
            onEmailChange = {},
            onWebsiteChange = {},
            onSaveClicked = {},
            onDismissDialog = {},
            onConfirmUpdateAuthor = {}
        )
    }
}
