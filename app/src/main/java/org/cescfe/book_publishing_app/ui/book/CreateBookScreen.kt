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
import org.cescfe.book_publishing_app.domain.book.model.enums.Status
import org.cescfe.book_publishing_app.domain.shared.enums.Genre
import org.cescfe.book_publishing_app.domain.shared.enums.Language
import org.cescfe.book_publishing_app.domain.shared.enums.ReadingLevel
import org.cescfe.book_publishing_app.ui.book.components.BookFormFields
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
        onTitleChange = viewModel::onTitleChange,
        onAuthorNameChange = viewModel::onAuthorNameChange,
        onCollectionNameChange = viewModel::onCollectionNameChange,
        onBasePriceChange = viewModel::onBasePriceChange,
        onReadingLevelChange = viewModel::onReadingLevelChange,
        onPrimaryLanguageChange = viewModel::onPrimaryLanguageChange,
        onSecondaryLanguagesChange = viewModel::onSecondaryLanguagesChange,
        onPrimaryGenreChange = viewModel::onPrimaryGenreChange,
        onSecondaryGenresChange = viewModel::onSecondaryGenresChange,
        onVatRateChange = viewModel::onVatRateChange,
        onIsbnChange = viewModel::onIsbnChange,
        onPublicationDateChange = viewModel::onPublicationDateChange,
        onPageCountChange = viewModel::onPageCountChange,
        onDescriptionChange = viewModel::onDescriptionChange,
        onStatusChange = viewModel::onStatusChange,
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
    onTitleChange: (String) -> Unit,
    onAuthorNameChange: (String) -> Unit,
    onCollectionNameChange: (String) -> Unit,
    onBasePriceChange: (String) -> Unit,
    onReadingLevelChange: (ReadingLevel?) -> Unit,
    onPrimaryLanguageChange: (Language?) -> Unit,
    onSecondaryLanguagesChange: (List<Language>) -> Unit,
    onPrimaryGenreChange: (Genre?) -> Unit,
    onSecondaryGenresChange: (List<Genre>) -> Unit,
    onVatRateChange: (String) -> Unit,
    onIsbnChange: (String) -> Unit,
    onPublicationDateChange: (String) -> Unit,
    onPageCountChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onStatusChange: (Status?) -> Unit,
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
                    BookFormFields(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        title = uiState.title,
                        onTitleChange = onTitleChange,
                        authorName = uiState.authorName,
                        onAuthorNameChange = onAuthorNameChange,
                        collectionName = uiState.collectionName,
                        onCollectionNameChange = onCollectionNameChange,
                        authors = uiState.authors,
                        collections = uiState.collections,
                        basePrice = uiState.basePrice,
                        onBasePriceChange = onBasePriceChange,
                        readingLevel = uiState.readingLevel,
                        onReadingLevelChange = onReadingLevelChange,
                        primaryLanguage = uiState.primaryLanguage,
                        onPrimaryLanguageChange = onPrimaryLanguageChange,
                        secondaryLanguages = uiState.secondaryLanguages,
                        onSecondaryLanguagesChange = onSecondaryLanguagesChange,
                        primaryGenre = uiState.primaryGenre,
                        onPrimaryGenreChange = onPrimaryGenreChange,
                        secondaryGenres = uiState.secondaryGenres,
                        onSecondaryGenresChange = onSecondaryGenresChange,
                        vatRate = uiState.vatRate,
                        onVatRateChange = onVatRateChange,
                        isbn = uiState.isbn,
                        onIsbnChange = onIsbnChange,
                        publicationDate = uiState.publicationDate,
                        onPublicationDateChange = onPublicationDateChange,
                        pageCount = uiState.pageCount,
                        onPageCountChange = onPageCountChange,
                        description = uiState.description,
                        onDescriptionChange = onDescriptionChange,
                        status = uiState.status,
                        onStatusChange = onStatusChange,
                        titleError = uiState.titleError?.let { stringResource(it) },
                        authorNameError = uiState.authorNameError?.let { stringResource(it) },
                        collectionNameError = uiState.collectionNameError?.let { stringResource(it) },
                        basePriceError = uiState.basePriceError?.let { stringResource(it) },
                        vatRateError = uiState.vatRateError?.let { stringResource(it) },
                        isbnError = uiState.isbnError?.let { stringResource(it) },
                        publicationDateError = uiState.publicationDateError?.let { stringResource(it) },
                        pageCountError = uiState.pageCountError?.let { stringResource(it) },
                        descriptionError = uiState.descriptionError?.let { stringResource(it) },
                        secondaryLanguagesError = uiState.secondaryLanguagesError?.let { stringResource(it) },
                        secondaryGenresError = uiState.secondaryGenresError?.let { stringResource(it) },
                        enabled = !uiState.isLoading
                    )
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
            onTitleChange = {},
            onAuthorNameChange = {},
            onCollectionNameChange = {},
            onBasePriceChange = {},
            onReadingLevelChange = {},
            onPrimaryLanguageChange = {},
            onSecondaryLanguagesChange = {},
            onPrimaryGenreChange = {},
            onSecondaryGenresChange = {},
            onVatRateChange = {},
            onIsbnChange = {},
            onPublicationDateChange = {},
            onPageCountChange = {},
            onDescriptionChange = {},
            onStatusChange = {},
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
            onTitleChange = {},
            onAuthorNameChange = {},
            onCollectionNameChange = {},
            onBasePriceChange = {},
            onReadingLevelChange = {},
            onPrimaryLanguageChange = {},
            onSecondaryLanguagesChange = {},
            onPrimaryGenreChange = {},
            onSecondaryGenresChange = {},
            onVatRateChange = {},
            onIsbnChange = {},
            onPublicationDateChange = {},
            onPageCountChange = {},
            onDescriptionChange = {},
            onStatusChange = {},
            onSaveClicked = {},
            onDismissDialog = {},
            onConfirmCreateBook = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateBookScreenWithDataPreview() {
    BookpublishingappTheme {
        CreateBookScreenContent(
            uiState = CreateBookUiState(
                title = "The Lord of the Rings",
                authorName = "J.R.R. Tolkien",
                collectionName = "Fantasy Classics",
                basePrice = "29.99",
                readingLevel = ReadingLevel.ADULT,
                primaryLanguage = Language.ENGLISH,
                primaryGenre = Genre.FANTASY
            ),
            onNavigateUp = {},
            onTitleChange = {},
            onAuthorNameChange = {},
            onCollectionNameChange = {},
            onBasePriceChange = {},
            onReadingLevelChange = {},
            onPrimaryLanguageChange = {},
            onSecondaryLanguagesChange = {},
            onPrimaryGenreChange = {},
            onSecondaryGenresChange = {},
            onVatRateChange = {},
            onIsbnChange = {},
            onPublicationDateChange = {},
            onPageCountChange = {},
            onDescriptionChange = {},
            onStatusChange = {},
            onSaveClicked = {},
            onDismissDialog = {},
            onConfirmCreateBook = {}
        )
    }
}
