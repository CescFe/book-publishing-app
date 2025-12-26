package org.cescfe.book_publishing_app.ui.book

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.cescfe.book_publishing_app.R
import org.cescfe.book_publishing_app.domain.author.model.AuthorSummary
import org.cescfe.book_publishing_app.domain.collection.model.CollectionSummary
import org.cescfe.book_publishing_app.domain.shared.enums.Genre
import org.cescfe.book_publishing_app.domain.shared.enums.Language
import org.cescfe.book_publishing_app.domain.shared.enums.ReadingLevel
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme
import org.junit.Rule
import org.junit.Test

class CreateBookScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun createBookScreen_displaysTopBarAndBottomBar() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                CreateBookScreen()
            }
        }

        composeTestRule
            .onNodeWithTag("create_book_top_bar")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithTag("save_button")
            .assertIsDisplayed()
    }

    @Test
    fun createBookScreen_validForm_saveClick_showsConfirmationDialog() {
        val authors = listOf(
            AuthorSummary(
                id = "550e8400-e29b-41d4-a716-446655440000",
                fullName = "J.R.R. Tolkien",
                pseudonym = "Tolkien",
                email = "tolkien@example.com"
            )
        )
        val collections = listOf(
            CollectionSummary(
                id = "550e8400-e29b-41d4-a716-446655440001",
                name = "Fantasy Classics",
                readingLevel = ReadingLevel.ADULT,
                primaryLanguage = Language.ENGLISH,
                primaryGenre = Genre.FANTASY
            )
        )

        composeTestRule.setContent {
            BookpublishingappTheme {
                CreateBookScreenContent(
                    uiState = CreateBookUiState(
                        title = "The Lord of the Rings",
                        authorName = "J.R.R. Tolkien",
                        collectionName = "Fantasy Classics",
                        basePrice = "29.99",
                        authors = authors,
                        collections = collections
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

        composeTestRule
            .onNodeWithTag("save_button")
            .performClick()
        composeTestRule
            .onNodeWithTag("confirmation_dialog")
            .assertIsDisplayed()
    }

    @Test
    fun createBookScreen_invalidForm_doesNotShowDialog() {
        composeTestRule.setContent {
            BookpublishingappTheme {
                CreateBookScreenContent(
                    uiState = CreateBookUiState(
                        title = "",
                        titleError = R.string.error_title_required
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

        composeTestRule
            .onNodeWithTag("confirmation_dialog")
            .assertDoesNotExist()
    }

    @Test
    fun createBookScreen_confirmSave_callsOnConfirmCreateBook() {
        var confirmCalled = false
        val authors = listOf(
            AuthorSummary(
                id = "550e8400-e29b-41d4-a716-446655440000",
                fullName = "J.R.R. Tolkien",
                pseudonym = "Tolkien",
                email = "tolkien@example.com"
            )
        )
        val collections = listOf(
            CollectionSummary(
                id = "550e8400-e29b-41d4-a716-446655440001",
                name = "Fantasy Classics",
                readingLevel = ReadingLevel.ADULT,
                primaryLanguage = Language.ENGLISH,
                primaryGenre = Genre.FANTASY
            )
        )

        composeTestRule.setContent {
            BookpublishingappTheme {
                CreateBookScreenContent(
                    uiState = CreateBookUiState(
                        title = "The Lord of the Rings",
                        authorName = "J.R.R. Tolkien",
                        collectionName = "Fantasy Classics",
                        basePrice = "29.99",
                        showConfirmDialog = true,
                        authors = authors,
                        collections = collections
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
                    onConfirmCreateBook = {
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
}
