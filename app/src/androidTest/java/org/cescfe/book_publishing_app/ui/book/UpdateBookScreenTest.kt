package org.cescfe.book_publishing_app.ui.book

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.cescfe.book_publishing_app.domain.book.model.enums.Status
import org.cescfe.book_publishing_app.domain.book.model.enums.VatRate
import org.cescfe.book_publishing_app.domain.shared.enums.Genre
import org.cescfe.book_publishing_app.domain.shared.enums.Language
import org.cescfe.book_publishing_app.domain.shared.enums.ReadingLevel
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme
import org.junit.Rule
import org.junit.Test

class UpdateBookScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun updateBookScreen_displaysTopBarAndBottomBar() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                UpdateBookScreenContent(
                    uiState = UpdateBookUiState(),
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
                    onConfirmUpdateBook = {}
                )
            }
        }

        composeTestRule
            .onNodeWithTag("update_book_top_bar")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithTag("save_button")
            .assertIsDisplayed()
    }

    @Test
    fun updateBookScreen_validForm_saveClick_showsConfirmationDialog() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                UpdateBookScreenContent(
                    uiState = UpdateBookUiState(
                        title = "The Lord of the Rings",
                        authorName = "J.R.R. Tolkien",
                        collectionName = "Fantasy Collection",
                        basePrice = "29.99",
                        showConfirmDialog = true
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
                    onConfirmUpdateBook = {}
                )
            }
        }

        composeTestRule
            .onNodeWithTag("confirmation_dialog")
            .assertIsDisplayed()
    }

    @Test
    fun updateBookScreen_confirmSave_callsOnConfirmUpdateBook() {
        var confirmCalled = false

        composeTestRule.setContent {
            BookpublishingappTheme {
                UpdateBookScreenContent(
                    uiState = UpdateBookUiState(
                        title = "The Lord of the Rings",
                        authorName = "J.R.R. Tolkien",
                        collectionName = "Fantasy Collection",
                        basePrice = "29.99",
                        showConfirmDialog = true
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
                    onConfirmUpdateBook = {
                        confirmCalled = true
                    }
                )
            }
        }

        composeTestRule
            .onNodeWithTag("confirm_button")
            .performClick()
        assert(confirmCalled)
    }

    @Test
    fun updateBookScreen_displaysPrefilledFormFields() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                UpdateBookScreenContent(
                    uiState = UpdateBookUiState(
                        title = "The Lord of the Rings",
                        authorName = "J.R.R. Tolkien",
                        collectionName = "Fantasy Collection",
                        basePrice = "29.99",
                        readingLevel = ReadingLevel.ADULT,
                        primaryLanguage = Language.ENGLISH,
                        secondaryLanguages = listOf(Language.SPANISH, Language.CATALAN),
                        primaryGenre = Genre.FANTASY,
                        secondaryGenres = listOf(Genre.ADVENTURE),
                        vatRate = VatRate.FOUR_PERCENT,
                        isbn = "978-0-544-00001-0",
                        publicationDate = "1954-07-29",
                        pageCount = "1178",
                        description = "Epic fantasy novel",
                        status = Status.PUBLISHED
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
                    onConfirmUpdateBook = {}
                )
            }
        }

        composeTestRule
            .onNodeWithTag("title_field")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithTag("author_name_field")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithTag("collection_name_field")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithTag("base_price_field")
            .assertIsDisplayed()
    }

    @Test
    fun updateBookScreen_showsLoadingState_whenLoading() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                UpdateBookScreenContent(
                    uiState = UpdateBookUiState(isLoading = true),
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
                    onConfirmUpdateBook = {}
                )
            }
        }

        composeTestRule
            .onNodeWithTag("loading_indicator")
            .assertIsDisplayed()
    }

    @Test
    fun updateBookScreen_dismissDialog_callsOnDismissDialog() {
        var dismissCalled = false

        composeTestRule.setContent {
            BookpublishingappTheme {
                UpdateBookScreenContent(
                    uiState = UpdateBookUiState(
                        title = "The Lord of the Rings",
                        authorName = "J.R.R. Tolkien",
                        collectionName = "Fantasy Collection",
                        basePrice = "29.99",
                        showConfirmDialog = true
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
                    onDismissDialog = {
                        dismissCalled = true
                    },
                    onConfirmUpdateBook = {}
                )
            }
        }

        composeTestRule
            .onNodeWithTag("dismiss_button")
            .performClick()
        assert(dismissCalled)
    }
}
