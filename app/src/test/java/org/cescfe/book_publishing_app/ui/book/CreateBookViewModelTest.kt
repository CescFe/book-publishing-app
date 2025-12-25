package org.cescfe.book_publishing_app.ui.book

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.cescfe.book_publishing_app.R
import org.cescfe.book_publishing_app.domain.book.model.Book
import org.cescfe.book_publishing_app.domain.book.model.enums.Status
import org.cescfe.book_publishing_app.domain.shared.DomainErrorType
import org.cescfe.book_publishing_app.domain.shared.DomainResult
import org.cescfe.book_publishing_app.domain.shared.enums.Genre
import org.cescfe.book_publishing_app.domain.shared.enums.Language
import org.cescfe.book_publishing_app.domain.shared.enums.ReadingLevel
import org.cescfe.book_publishing_app.ui.book.helper.MockBooksRepository
import org.cescfe.book_publishing_app.ui.book.helper.TestBookFactory
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreateBookViewModelTest {

    private lateinit var mockRepository: MockBooksRepository

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        mockRepository = MockBooksRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(): CreateBookViewModel = CreateBookViewModel(mockRepository)

    // ==================== SUCCESS CASES ====================

    @Test
    fun `createBook with all fields should return createdBookId`() = runTest {
        val createdBook = TestBookFactory.createBook(
            id = "book-456",
            title = "The Lord of the Rings",
            basePrice = 29.99,
            authorName = "J.R.R. Tolkien",
            collectionName = "Fantasy Collection"
        )
        mockRepository.createBookResult = DomainResult.Success(createdBook)

        val viewModel = createViewModel()
        viewModel.onTitleChange("The Lord of the Rings")
        viewModel.onAuthorIdChange("550e8400-e29b-41d4-a716-446655440000")
        viewModel.onCollectionIdChange("550e8400-e29b-41d4-a716-446655440001")
        viewModel.onBasePriceChange("29.99")
        viewModel.onReadingLevelChange(ReadingLevel.ADULT)
        viewModel.onPrimaryLanguageChange(Language.ENGLISH)
        viewModel.onSecondaryLanguagesChange(listOf(Language.SPANISH, Language.CATALAN))
        viewModel.onPrimaryGenreChange(Genre.FANTASY)
        viewModel.onSecondaryGenresChange(listOf(Genre.ADVENTURE))
        viewModel.onVatRateChange("0.04")
        viewModel.onIsbnChange("9780747591054")
        viewModel.onPublicationDateChange("1954-07-29")
        viewModel.onPageCountChange("1178")
        viewModel.onDescriptionChange("Epic fantasy novel")
        viewModel.onStatusChange(Status.PUBLISHED)
        viewModel.createBook()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("book-456", state.createdBookId)
        assertEquals("The Lord of the Rings", state.title)
        assertEquals("550e8400-e29b-41d4-a716-446655440000", state.authorId)
        assertEquals("550e8400-e29b-41d4-a716-446655440001", state.collectionId)
        assertEquals("29.99", state.basePrice)
        assertEquals(ReadingLevel.ADULT, state.readingLevel)
        assertEquals(Language.ENGLISH, state.primaryLanguage)
        assertEquals(2, state.secondaryLanguages.size)
        assertEquals(Genre.FANTASY, state.primaryGenre)
        assertEquals(1, state.secondaryGenres.size)
        assertEquals("0.04", state.vatRate)
        assertEquals("9780747591054", state.isbn)
        assertEquals("1954-07-29", state.publicationDate)
        assertEquals("1178", state.pageCount)
        assertEquals("Epic fantasy novel", state.description)
        assertEquals(Status.PUBLISHED, state.status)
    }

    @Test
    fun `createBook with required fields only should return createdBookId`() = runTest {
        val createdBook = Book(
            id = "book-123",
            title = "1984",
            basePrice = 15.50,
            authorName = "George Orwell",
            collectionName = "Classics",
            readingLevel = null,
            primaryLanguage = null,
            secondaryLanguages = emptyList(),
            primaryGenre = null,
            secondaryGenres = emptyList(),
            vatRate = 0.04,
            finalPrice = 16.12,
            isbn = null,
            publicationDate = null,
            pageCount = null,
            description = null,
            status = Status.DRAFT
        )
        mockRepository.createBookResult = DomainResult.Success(createdBook)

        val viewModel = createViewModel()
        viewModel.onTitleChange("1984")
        viewModel.onAuthorIdChange("550e8400-e29b-41d4-a716-446655440002")
        viewModel.onCollectionIdChange("550e8400-e29b-41d4-a716-446655440003")
        viewModel.onBasePriceChange("15.50")
        viewModel.createBook()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("book-123", state.createdBookId)
    }

    // ==================== VALIDATION CASES ====================

    @Test
    fun `form is invalid when title is blank`() = runTest {
        val viewModel = createViewModel()
        viewModel.onTitleChange("   ")

        val state = viewModel.uiState.value
        assertFalse(state.isFormValid)
        assertEquals(R.string.error_title_required, state.titleError)
    }

    @Test
    fun `form is invalid when title is empty`() = runTest {
        val viewModel = createViewModel()

        val state = viewModel.uiState.value
        assertFalse(state.isFormValid)
    }

    @Test
    fun `form is invalid when authorId is empty`() = runTest {
        val viewModel = createViewModel()
        viewModel.onTitleChange("Test Book")
        viewModel.onCollectionIdChange("collection-123")
        viewModel.onBasePriceChange("10.0")

        val state = viewModel.uiState.value
        assertFalse(state.isFormValid)
    }

    @Test
    fun `form is invalid when collectionId is empty`() = runTest {
        val viewModel = createViewModel()
        viewModel.onTitleChange("Test Book")
        viewModel.onAuthorIdChange("author-123")
        viewModel.onBasePriceChange("10.0")

        val state = viewModel.uiState.value
        assertFalse(state.isFormValid)
    }

    @Test
    fun `form is invalid when basePrice is empty`() = runTest {
        val viewModel = createViewModel()
        viewModel.onTitleChange("Test Book")
        viewModel.onAuthorIdChange("author-123")
        viewModel.onCollectionIdChange("collection-123")

        val state = viewModel.uiState.value
        assertFalse(state.isFormValid)
    }

    @Test
    fun `form is invalid when title is too long`() = runTest {
        val viewModel = createViewModel()
        viewModel.onTitleChange("a".repeat(201))

        val state = viewModel.uiState.value
        assertFalse(state.isFormValid)
        assertEquals(R.string.error_title_too_long, state.titleError)
    }

    @Test
    fun `form is invalid when authorId is not a valid UUID`() = runTest {
        val viewModel = createViewModel()
        viewModel.onAuthorIdChange("invalid-uuid")

        val state = viewModel.uiState.value
        assertFalse(state.isFormValid)
        assertEquals(R.string.error_author_id_invalid_format, state.authorIdError)
    }

    @Test
    fun `form is invalid when collectionId is not a valid UUID`() = runTest {
        val viewModel = createViewModel()
        viewModel.onCollectionIdChange("invalid-uuid")

        val state = viewModel.uiState.value
        assertFalse(state.isFormValid)
        assertEquals(R.string.error_collection_id_invalid_format, state.collectionIdError)
    }

    @Test
    fun `form is invalid when basePrice is negative`() = runTest {
        val viewModel = createViewModel()
        viewModel.onBasePriceChange("-10.0")

        val state = viewModel.uiState.value
        assertFalse(state.isFormValid)
        assertEquals(R.string.error_base_price_negative, state.basePriceError)
    }

    @Test
    fun `form is invalid when basePrice has more than 2 decimal places`() = runTest {
        val viewModel = createViewModel()
        viewModel.onBasePriceChange("10.999")

        val state = viewModel.uiState.value
        assertFalse(state.isFormValid)
        assertEquals(R.string.error_base_price_invalid_precision, state.basePriceError)
    }

    @Test
    fun `form is invalid when vatRate is greater than 1`() = runTest {
        val viewModel = createViewModel()
        viewModel.onVatRateChange("1.5")

        val state = viewModel.uiState.value
        assertEquals(R.string.error_vat_rate_too_high, state.vatRateError)
    }

    @Test
    fun `form is invalid when isbn format is invalid`() = runTest {
        val viewModel = createViewModel()
        viewModel.onIsbnChange("invalid-isbn")

        val state = viewModel.uiState.value
        assertEquals(R.string.error_isbn_invalid, state.isbnError)
    }

    @Test
    fun `form is invalid when publicationDate format is invalid`() = runTest {
        val viewModel = createViewModel()
        viewModel.onPublicationDateChange("2024/01/01")

        val state = viewModel.uiState.value
        assertEquals(R.string.error_publication_date_invalid_format, state.publicationDateError)
    }

    @Test
    fun `form is invalid when pageCount is less than 1`() = runTest {
        val viewModel = createViewModel()
        viewModel.onPageCountChange("0")

        val state = viewModel.uiState.value
        assertEquals(R.string.error_page_count_invalid, state.pageCountError)
    }

    @Test
    fun `form is invalid when description is too long`() = runTest {
        val viewModel = createViewModel()
        viewModel.onDescriptionChange("a".repeat(2001))

        val state = viewModel.uiState.value
        assertEquals(R.string.error_description_too_long, state.descriptionError)
    }

    @Test
    fun `form is invalid when secondaryLanguages exceed 3`() = runTest {
        val viewModel = createViewModel()
        viewModel.onSecondaryLanguagesChange(
            listOf(Language.ENGLISH, Language.SPANISH, Language.CATALAN, Language.VALENCIAN)
        )

        val state = viewModel.uiState.value
        assertFalse(state.isFormValid)
        assertEquals(R.string.error_secondary_languages_too_many, state.secondaryLanguagesError)
    }

    @Test
    fun `form is invalid when secondaryLanguages contain duplicates`() = runTest {
        val viewModel = createViewModel()
        viewModel.onSecondaryLanguagesChange(
            listOf(Language.ENGLISH, Language.SPANISH, Language.ENGLISH)
        )

        val state = viewModel.uiState.value
        assertFalse(state.isFormValid)
        assertEquals(R.string.error_secondary_languages_duplicated, state.secondaryLanguagesError)
    }

    @Test
    fun `form is invalid when secondaryLanguages contain primaryLanguage`() = runTest {
        val viewModel = createViewModel()
        viewModel.onPrimaryLanguageChange(Language.ENGLISH)
        viewModel.onSecondaryLanguagesChange(listOf(Language.ENGLISH, Language.SPANISH))

        val state = viewModel.uiState.value
        assertFalse(state.isFormValid)
        assertEquals(R.string.error_secondary_language_same_as_primary, state.secondaryLanguagesError)
    }

    @Test
    fun `form is invalid when secondaryGenres exceed 3`() = runTest {
        val viewModel = createViewModel()
        viewModel.onSecondaryGenresChange(
            listOf(Genre.FANTASY, Genre.ADVENTURE, Genre.MYSTERY, Genre.THRILLER)
        )

        val state = viewModel.uiState.value
        assertFalse(state.isFormValid)
        assertEquals(R.string.error_secondary_genres_too_many, state.secondaryGenresError)
    }

    @Test
    fun `form is invalid when secondaryGenres contain duplicates`() = runTest {
        val viewModel = createViewModel()
        viewModel.onSecondaryGenresChange(
            listOf(Genre.FANTASY, Genre.ADVENTURE, Genre.FANTASY)
        )

        val state = viewModel.uiState.value
        assertFalse(state.isFormValid)
        assertEquals(R.string.error_secondary_genres_duplicated, state.secondaryGenresError)
    }

    @Test
    fun `form is invalid when secondaryGenres contain primaryGenre`() = runTest {
        val viewModel = createViewModel()
        viewModel.onPrimaryGenreChange(Genre.FANTASY)
        viewModel.onSecondaryGenresChange(listOf(Genre.FANTASY, Genre.ADVENTURE))

        val state = viewModel.uiState.value
        assertFalse(state.isFormValid)
        assertEquals(R.string.error_secondary_genre_same_as_primary, state.secondaryGenresError)
    }

    @Test
    fun `createBook does nothing when form is invalid`() = runTest {
        val viewModel = createViewModel()
        viewModel.createBook()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNull(state.createdBookId)
        assertFalse(state.isLoading)
    }

    // ==================== CONFIRMATION DIALOG ====================

    @Test
    fun `onSaveClicked with valid form should show confirm dialog`() = runTest {
        val viewModel = createViewModel()
        viewModel.onTitleChange("Test Book")
        viewModel.onAuthorIdChange("550e8400-e29b-41d4-a716-446655440000")
        viewModel.onCollectionIdChange("550e8400-e29b-41d4-a716-446655440001")
        viewModel.onBasePriceChange("10.0")
        viewModel.onSaveClicked()

        val state = viewModel.uiState.value
        assertTrue(state.showConfirmDialog)
    }

    @Test
    fun `onSaveClicked with invalid form should not show confirm dialog`() = runTest {
        val viewModel = createViewModel()
        viewModel.onSaveClicked()

        val state = viewModel.uiState.value
        assertFalse(state.showConfirmDialog)
    }

    @Test
    fun `dismissConfirmDialog should hide confirm dialog`() = runTest {
        val viewModel = createViewModel()
        viewModel.onTitleChange("Test Book")
        viewModel.onAuthorIdChange("550e8400-e29b-41d4-a716-446655440000")
        viewModel.onCollectionIdChange("550e8400-e29b-41d4-a716-446655440001")
        viewModel.onBasePriceChange("10.0")
        viewModel.onSaveClicked()
        viewModel.dismissConfirmDialog()

        val state = viewModel.uiState.value
        assertFalse(state.showConfirmDialog)
    }

    // ==================== SESSION EXPIRED ====================

    @Test
    fun `createBook with UNAUTHORIZED error should set sessionExpired`() = runTest {
        mockRepository.createBookResult = DomainResult.Error(DomainErrorType.UNAUTHORIZED)

        val viewModel = createViewModel()
        viewModel.onTitleChange("Test Book")
        viewModel.onAuthorIdChange("550e8400-e29b-41d4-a716-446655440000")
        viewModel.onCollectionIdChange("550e8400-e29b-41d4-a716-446655440001")
        viewModel.onBasePriceChange("10.0")
        viewModel.createBook()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.sessionExpired)
        assertNull(state.errorResId)
    }

    @Test
    fun `onSessionExpiredHandled should clear sessionExpired flag`() = runTest {
        mockRepository.createBookResult = DomainResult.Error(DomainErrorType.UNAUTHORIZED)

        val viewModel = createViewModel()
        viewModel.onTitleChange("Test Book")
        viewModel.onAuthorIdChange("550e8400-e29b-41d4-a716-446655440000")
        viewModel.onCollectionIdChange("550e8400-e29b-41d4-a716-446655440001")
        viewModel.onBasePriceChange("10.0")
        viewModel.createBook()
        advanceUntilIdle()
        viewModel.onSessionExpiredHandled()

        val state = viewModel.uiState.value
        assertFalse(state.sessionExpired)
    }

    // ==================== ERROR HANDLING ====================

    @Test
    fun `createBook with network error should set errorResId`() = runTest {
        mockRepository.createBookResult = DomainResult.Error(DomainErrorType.NETWORK_ERROR)

        val viewModel = createViewModel()
        viewModel.onTitleChange("Test Book")
        viewModel.onAuthorIdChange("550e8400-e29b-41d4-a716-446655440000")
        viewModel.onCollectionIdChange("550e8400-e29b-41d4-a716-446655440001")
        viewModel.onBasePriceChange("10.0")
        viewModel.createBook()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.sessionExpired)
        assertFalse(state.isLoading)
        assertNull(state.createdBookId)
    }
}
