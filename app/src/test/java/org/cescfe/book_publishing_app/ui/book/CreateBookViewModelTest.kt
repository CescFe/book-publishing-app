package org.cescfe.book_publishing_app.ui.book

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.cescfe.book_publishing_app.R
import org.cescfe.book_publishing_app.domain.author.model.AuthorSummary
import org.cescfe.book_publishing_app.domain.book.model.Book
import org.cescfe.book_publishing_app.domain.book.model.enums.Status
import org.cescfe.book_publishing_app.domain.collection.model.CollectionSummary
import org.cescfe.book_publishing_app.domain.shared.DomainErrorType
import org.cescfe.book_publishing_app.domain.shared.DomainResult
import org.cescfe.book_publishing_app.domain.shared.enums.Genre
import org.cescfe.book_publishing_app.domain.shared.enums.Language
import org.cescfe.book_publishing_app.domain.shared.enums.ReadingLevel
import org.cescfe.book_publishing_app.ui.author.helper.MockAuthorsRepository
import org.cescfe.book_publishing_app.ui.book.helper.MockBooksRepository
import org.cescfe.book_publishing_app.ui.book.helper.TestBookFactory
import org.cescfe.book_publishing_app.ui.collection.helper.MockCollectionsRepository
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreateBookViewModelTest {

    private lateinit var mockBooksRepository: MockBooksRepository
    private lateinit var mockAuthorsRepository: MockAuthorsRepository
    private lateinit var mockCollectionsRepository: MockCollectionsRepository

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        mockBooksRepository = MockBooksRepository()
        mockAuthorsRepository = MockAuthorsRepository()
        mockCollectionsRepository = MockCollectionsRepository()

        setupDefaultAuthors()
        setupDefaultCollections()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun setupDefaultAuthors() {
        mockAuthorsRepository.authorsResult = DomainResult.Success(
            listOf(
                AuthorSummary(
                    id = "550e8400-e29b-41d4-a716-446655440000",
                    fullName = "J.R.R. Tolkien",
                    pseudonym = "Tolkien",
                    email = "tolkien@example.com"
                ),
                AuthorSummary(
                    id = "550e8400-e29b-41d4-a716-446655440002",
                    fullName = "George Orwell",
                    pseudonym = null,
                    email = "orwell@example.com"
                ),
                AuthorSummary(
                    id = "test-author-id",
                    fullName = "Test Author",
                    pseudonym = null,
                    email = null
                )
            )
        )
    }

    private fun setupDefaultCollections() {
        mockCollectionsRepository.collectionsResult = DomainResult.Success(
            listOf(
                CollectionSummary(
                    id = "550e8400-e29b-41d4-a716-446655440001",
                    name = "Fantasy Collection",
                    readingLevel = null,
                    primaryLanguage = null,
                    primaryGenre = null
                ),
                CollectionSummary(
                    id = "550e8400-e29b-41d4-a716-446655440003",
                    name = "Classics",
                    readingLevel = null,
                    primaryLanguage = null,
                    primaryGenre = null
                ),
                CollectionSummary(
                    id = "test-collection-id",
                    name = "Test Collection",
                    readingLevel = null,
                    primaryLanguage = null,
                    primaryGenre = null
                )
            )
        )
    }

    private fun createViewModel(): CreateBookViewModel = CreateBookViewModel(
        mockBooksRepository,
        mockAuthorsRepository,
        mockCollectionsRepository
    )

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
        mockBooksRepository.createBookResult = DomainResult.Success(createdBook)

        val viewModel = createViewModel()

        viewModel.onTitleChange("The Lord of the Rings")
        viewModel.onAuthorNameChange("J.R.R. Tolkien")
        viewModel.onCollectionNameChange("Fantasy Collection")
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

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("book-456", state.createdBookId)
        assertEquals("The Lord of the Rings", state.title)
        assertEquals("J.R.R. Tolkien", state.authorName)
        assertEquals("Fantasy Collection", state.collectionName)
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
        mockBooksRepository.createBookResult = DomainResult.Success(createdBook)

        val viewModel = createViewModel()

        viewModel.onTitleChange("1984")
        viewModel.onAuthorNameChange("George Orwell")
        viewModel.onCollectionNameChange("Classics")
        viewModel.onBasePriceChange("15.50")
        viewModel.createBook()

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
    fun `form is invalid when authorName is empty`() = runTest {
        val viewModel = createViewModel()
        viewModel.onTitleChange("Test Book")
        viewModel.onCollectionNameChange("Test Collection")
        viewModel.onBasePriceChange("10.0")

        val state = viewModel.uiState.value
        assertFalse(state.isFormValid)
    }

    @Test
    fun `form is invalid when collectionName is empty`() = runTest {
        val viewModel = createViewModel()
        viewModel.onTitleChange("Test Book")
        viewModel.onAuthorNameChange("Test Author")
        viewModel.onBasePriceChange("10.0")

        val state = viewModel.uiState.value
        assertFalse(state.isFormValid)
    }

    @Test
    fun `form is invalid when basePrice is empty`() = runTest {
        val viewModel = createViewModel()
        viewModel.onTitleChange("Test Book")
        viewModel.onAuthorNameChange("Test Author")
        viewModel.onCollectionNameChange("Test Collection")

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
    fun `form is invalid when authorName does not exist`() = runTest {
        val viewModel = createViewModel()
        viewModel.onAuthorNameChange("Non-existent Author")

        val state = viewModel.uiState.value
        assertFalse(state.isFormValid)
        assertEquals(R.string.error_author_id_invalid_format, state.authorNameError)
    }

    @Test
    fun `form is invalid when collectionName does not exist`() = runTest {
        val viewModel = createViewModel()
        viewModel.onCollectionNameChange("Non-existent Collection")

        val state = viewModel.uiState.value
        assertFalse(state.isFormValid)
        assertEquals(R.string.error_collection_id_not_found, state.collectionNameError)
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

        val state = viewModel.uiState.value
        assertNull(state.createdBookId)
        assertFalse(state.isLoading)
    }

    // ==================== CONFIRMATION DIALOG ====================

    @Test
    fun `onSaveClicked with valid form should show confirm dialog`() = runTest {
        val viewModel = createViewModel()
        viewModel.onTitleChange("Test Book")
        viewModel.onAuthorNameChange("Test Author")
        viewModel.onCollectionNameChange("Test Collection")
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
        viewModel.onAuthorNameChange("Test Author")
        viewModel.onCollectionNameChange("Test Collection")
        viewModel.onBasePriceChange("10.0")
        viewModel.onSaveClicked()
        viewModel.dismissConfirmDialog()

        val state = viewModel.uiState.value
        assertFalse(state.showConfirmDialog)
    }

    // ==================== SESSION EXPIRED ====================

    @Test
    fun `createBook with UNAUTHORIZED error should set sessionExpired`() = runTest {
        mockBooksRepository.createBookResult = DomainResult.Error(DomainErrorType.UNAUTHORIZED)

        val viewModel = createViewModel()
        viewModel.onTitleChange("Test Book")
        viewModel.onAuthorNameChange("Test Author")
        viewModel.onCollectionNameChange("Test Collection")
        viewModel.onBasePriceChange("10.0")
        viewModel.createBook()

        val state = viewModel.uiState.value
        assertTrue(state.sessionExpired)
        assertNull(state.errorResId)
    }

    @Test
    fun `onSessionExpiredHandled should clear sessionExpired flag`() = runTest {
        mockBooksRepository.createBookResult = DomainResult.Error(DomainErrorType.UNAUTHORIZED)

        val viewModel = createViewModel()
        viewModel.onTitleChange("Test Book")
        viewModel.onAuthorNameChange("Test Author")
        viewModel.onCollectionNameChange("Test Collection")
        viewModel.onBasePriceChange("10.0")
        viewModel.createBook()
        viewModel.onSessionExpiredHandled()

        val state = viewModel.uiState.value
        assertFalse(state.sessionExpired)
    }

    // ==================== ERROR HANDLING ====================

    @Test
    fun `createBook with network error should set errorResId`() = runTest {
        mockBooksRepository.createBookResult = DomainResult.Error(DomainErrorType.NETWORK_ERROR)

        val viewModel = createViewModel()
        viewModel.onTitleChange("Test Book")
        viewModel.onAuthorNameChange("Test Author")
        viewModel.onCollectionNameChange("Test Collection")
        viewModel.onBasePriceChange("10.0")
        viewModel.createBook()

        val state = viewModel.uiState.value
        assertFalse(state.sessionExpired)
        assertFalse(state.isLoading)
        assertNull(state.createdBookId)
    }
}
