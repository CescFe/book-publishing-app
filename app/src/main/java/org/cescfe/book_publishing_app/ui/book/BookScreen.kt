package org.cescfe.book_publishing_app.ui.book

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
import org.cescfe.book_publishing_app.ui.book.components.BookCard
import org.cescfe.book_publishing_app.ui.shared.components.ErrorState
import org.cescfe.book_publishing_app.ui.shared.components.LoadingState
import org.cescfe.book_publishing_app.ui.shared.navigation.DetailActionsBottomBar
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookScreen(
    bookId: String,
    viewModel: BookViewModel = viewModel(),
    onSessionExpired: () -> Unit = {},
    onNavigateUp: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(bookId) {
        viewModel.loadBook(bookId)
    }

    LaunchedEffect(uiState.sessionExpired) {
        if (uiState.sessionExpired) {
            onSessionExpired()
            viewModel.onSessionExpiredHandled()
        }
    }

    BookScreenContent(
        uiState = uiState,
        onRetry = viewModel::retry,
        onNavigateUp = onNavigateUp,
        onEditClick = onEditClick,
        onDeleteClick = onDeleteClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BookScreenContent(
    uiState: BookUiState,
    onRetry: () -> Unit,
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
                uiState.book != null -> {
                    BookCard(
                        book = uiState.book,
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
private fun BookScreenLoadingPreview() {
    BookpublishingappTheme {
        BookScreenContent(
            uiState = BookUiState(isLoading = true),
            onRetry = {},
            onNavigateUp = {},
            onEditClick = {},
            onDeleteClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookScreenErrorPreview() {
    BookpublishingappTheme {
        BookScreenContent(
            uiState = BookUiState(errorResId = R.string.error_network),
            onRetry = {},
            onNavigateUp = {},
            onEditClick = {},
            onDeleteClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookScreenSuccessPreview() {
    BookpublishingappTheme {
        BookScreenContent(
            uiState = BookUiState(
                book = org.cescfe.book_publishing_app.domain.book.model.Book(
                    id = "1",
                    title = "Harry Potter and the Deathly Hallows",
                    basePrice = 29.99,
                    authorName = "J.K. Rowling",
                    collectionName = "Harry Potter Series",
                    readingLevel = org.cescfe.book_publishing_app.domain.shared.enums.ReadingLevel.YOUNG_ADULT,
                    primaryLanguage = org.cescfe.book_publishing_app.domain.shared.enums.Language.ENGLISH,
                    secondaryLanguages = listOf(
                        org.cescfe.book_publishing_app.domain.shared.enums.Language.SPANISH,
                        org.cescfe.book_publishing_app.domain.shared.enums.Language.CATALAN
                    ),
                    primaryGenre = org.cescfe.book_publishing_app.domain.shared.enums.Genre.FANTASY,
                    secondaryGenres = listOf(
                        org.cescfe.book_publishing_app.domain.shared.enums.Genre.ADVENTURE,
                        org.cescfe.book_publishing_app.domain.shared.enums.Genre.MYSTERY
                    ),
                    vatRate = 0.04,
                    finalPrice = 31.19,
                    isbn = "9780747591054",
                    publicationDate = "2007-07-21",
                    pageCount = 607,
                    description = "Harry, Ron, and Hermione hunt for Horcruxes",
                    status = org.cescfe.book_publishing_app.domain.book.model.enums.Status.PUBLISHED
                )
            ),
            onRetry = {},
            onNavigateUp = {},
            onEditClick = {},
            onDeleteClick = {}
        )
    }
}
