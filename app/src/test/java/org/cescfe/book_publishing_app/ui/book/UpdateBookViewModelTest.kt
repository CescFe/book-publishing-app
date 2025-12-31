package org.cescfe.book_publishing_app.ui.book

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.cescfe.book_publishing_app.R
import org.cescfe.book_publishing_app.domain.author.model.AuthorSummary
import org.cescfe.book_publishing_app.domain.book.model.enums.Status
import org.cescfe.book_publishing_app.domain.book.model.enums.VatRate
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
class UpdateBookViewModelTest {

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
                    id = "author-123",
                    fullName = "J.R.R. Tolkien",
                    pseudonym = "Tolkien",
                    email = "tolkien@example.com"
                ),
                AuthorSummary(
                    id = "author-456",
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
                    id = "collection-123",
                    name = "Fantasy Collection",
                    readingLevel = null,
                    primaryLanguage = null,
                    primaryGenre = null
                ),
                CollectionSummary(
                    id = "collection-456",
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

    private fun createViewModel(): UpdateBookViewModel = UpdateBookViewModel(
        mockBooksRepository,
        mockAuthorsRepository,
        mockCollectionsRepository
    )

    // ==================== LOAD BOOK CASES ====================

    @Test
    fun `loadBook with valid bookId should prefilled form fields`() = runTest {
        val book = TestBookFactory.createBook(
            id = "book-123",
            title = "The Lord of the Rings",
            basePrice = 29.99,
            authorName = "J.R.R. Tolkien",
            collectionName = "Fantasy Collection",
            readingLevel = ReadingLevel.ADULT,
            primaryLanguage = Language.ENGLISH,
            secondaryLanguages = listOf(Language.SPANISH, Language.CATALAN),
            primaryGenre = Genre.FANTASY,
            secondaryGenres = listOf(Genre.ADVENTURE),
            vatRate = 0.04,
            isbn = "978-0-544-00001-0",
            publicationDate = "1954-07-29",
            pageCount = 1178,
            description = "Epic fantasy novel",
            status = Status.PUBLISHED
        )
        mockBooksRepository.bookResult = DomainResult.Success(book)

        val viewModel = createViewModel()
        viewModel.loadBook("book-123")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("The Lord of the Rings", state.title)
        assertEquals("J.R.R. Tolkien", state.authorName)
        assertEquals("Fantasy Collection", state.collectionName)
        assertEquals("29.99", state.basePrice)
        assertEquals(ReadingLevel.ADULT, state.readingLevel)
        assertEquals(Language.ENGLISH, state.primaryLanguage)
        assertEquals(2, state.secondaryLanguages.size)
        assertEquals(Genre.FANTASY, state.primaryGenre)
        assertEquals(1, state.secondaryGenres.size)
        assertEquals(VatRate.FOUR_PERCENT, state.vatRate)
        assertEquals("978-0-544-00001-0", state.isbn)
        assertEquals("1954-07-29", state.publicationDate)
        assertEquals("1178", state.pageCount)
        assertEquals("Epic fantasy novel", state.description)
        assertEquals(Status.PUBLISHED, state.status)
    }

    @Test
    fun `loadBook with book having null fields should use empty strings`() = runTest {
        val book = TestBookFactory.createBook(
            id = "book-456",
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
            isbn = null,
            publicationDate = null,
            pageCount = null,
            description = null,
            status = Status.DRAFT
        )
        mockBooksRepository.bookResult = DomainResult.Success(book)

        val viewModel = createViewModel()
        viewModel.loadBook("book-456")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("1984", state.title)
        assertEquals("George Orwell", state.authorName)
        assertEquals("Classics", state.collectionName)
        assertEquals("15.5", state.basePrice)
        assertEquals("", state.isbn)
        assertEquals("", state.publicationDate)
        assertEquals("", state.pageCount)
        assertEquals("", state.description)
    }

    @Test
    fun `loadBook with error should set error state`() = runTest {
        mockBooksRepository.bookResult = DomainResult.Error(DomainErrorType.NETWORK_ERROR)

        val viewModel = createViewModel()
        viewModel.loadBook("book-123")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.errorResId != null)
    }

    // ==================== UPDATE BOOK SUCCESS CASES ====================

    @Test
    fun `updateBook with all fields should return updatedBookId`() = runTest {
        val book = TestBookFactory.createBook(id = "book-123")
        mockBooksRepository.bookResult = DomainResult.Success(book)
        mockBooksRepository.updateBookResult = DomainResult.Success(
            TestBookFactory.createBook(id = "book-123")
        )

        val viewModel = createViewModel()
        viewModel.loadBook("book-123")
        advanceUntilIdle()

        viewModel.onTitleChange("The Lord of the Rings")
        viewModel.onAuthorNameChange("J.R.R. Tolkien")
        viewModel.onCollectionNameChange("Fantasy Collection")
        viewModel.onBasePriceChange("29.99")
        viewModel.onReadingLevelChange(ReadingLevel.ADULT)
        viewModel.onPrimaryLanguageChange(Language.ENGLISH)
        viewModel.onSecondaryLanguagesChange(listOf(Language.SPANISH, Language.CATALAN))
        viewModel.onPrimaryGenreChange(Genre.FANTASY)
        viewModel.onSecondaryGenresChange(listOf(Genre.ADVENTURE))
        viewModel.onVatRateChange(VatRate.FOUR_PERCENT)
        viewModel.onIsbnChange("978-0-544-00001-0")
        viewModel.onPublicationDateChange("1954-07-29")
        viewModel.onPageCountChange("1178")
        viewModel.onDescriptionChange("Epic fantasy novel")
        viewModel.onStatusChange(Status.PUBLISHED)
        viewModel.updateBook()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("book-123", state.updatedBookId)
        assertEquals("The Lord of the Rings", state.title)
        assertEquals("J.R.R. Tolkien", state.authorName)
        assertEquals("Fantasy Collection", state.collectionName)
        assertEquals("29.99", state.basePrice)
    }

    @Test
    fun `updateBook converts blank strings to null for optional fields`() = runTest {
        val book = TestBookFactory.createBook(id = "book-123")
        mockBooksRepository.bookResult = DomainResult.Success(book)
        mockBooksRepository.updateBookResult = DomainResult.Success(
            TestBookFactory.createBook(id = "book-123")
        )

        val viewModel = createViewModel()
        viewModel.loadBook("book-123")
        advanceUntilIdle()

        viewModel.onTitleChange("1984")
        viewModel.onAuthorNameChange("George Orwell")
        viewModel.onCollectionNameChange("Classics")
        viewModel.onBasePriceChange("15.50")
        viewModel.onIsbnChange("   ")
        viewModel.onPublicationDateChange("")
        viewModel.onPageCountChange("  ")
        viewModel.onDescriptionChange("")
        viewModel.updateBook()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("book-123", state.updatedBookId)
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
    fun `updateBook does nothing when form is invalid`() = runTest {
        val book = TestBookFactory.createBook(id = "book-123")
        mockBooksRepository.bookResult = DomainResult.Success(book)

        val viewModel = createViewModel()
        viewModel.loadBook("book-123")
        advanceUntilIdle()

        viewModel.updateBook()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNull(state.updatedBookId)
        assertFalse(state.isLoading)
    }

    @Test
    fun `updateBook does nothing when bookId is not set`() = runTest {
        val viewModel = createViewModel()
        viewModel.onTitleChange("Test Book")
        viewModel.onAuthorNameChange("Test Author")
        viewModel.onCollectionNameChange("Test Collection")
        viewModel.onBasePriceChange("10.0")
        viewModel.updateBook()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNull(state.updatedBookId)
        assertFalse(state.isLoading)
    }

    // ==================== SESSION EXPIRED ====================

    @Test
    fun `updateBook with UNAUTHORIZED error should set sessionExpired`() = runTest {
        val book = TestBookFactory.createBook(id = "book-123")
        mockBooksRepository.bookResult = DomainResult.Success(book)
        mockBooksRepository.updateBookResult = DomainResult.Error(DomainErrorType.UNAUTHORIZED)

        val viewModel = createViewModel()
        viewModel.loadBook("book-123")
        advanceUntilIdle()

        viewModel.onTitleChange("Test Book")
        viewModel.onAuthorNameChange("J.R.R. Tolkien")
        viewModel.onCollectionNameChange("Fantasy Collection")
        viewModel.onBasePriceChange("10.0")
        viewModel.updateBook()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.sessionExpired)
        assertNull(state.errorResId)
    }

    @Test
    fun `loadBook with UNAUTHORIZED error should set sessionExpired`() = runTest {
        mockBooksRepository.bookResult = DomainResult.Error(DomainErrorType.UNAUTHORIZED)

        val viewModel = createViewModel()
        viewModel.loadBook("book-123")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.sessionExpired)
        assertNull(state.errorResId)
    }

    // ==================== CONFIRMATION DIALOG ====================

    @Test
    fun `onSaveClicked with valid form should show confirmation dialog`() = runTest {
        val book = TestBookFactory.createBook(id = "book-123")
        mockBooksRepository.bookResult = DomainResult.Success(book)

        val viewModel = createViewModel()
        viewModel.loadBook("book-123")
        advanceUntilIdle()

        viewModel.onTitleChange("Test Book")
        viewModel.onAuthorNameChange("J.R.R. Tolkien")
        viewModel.onCollectionNameChange("Fantasy Collection")
        viewModel.onBasePriceChange("10.0")
        viewModel.onSaveClicked()

        val state = viewModel.uiState.value
        assertTrue(state.showConfirmDialog)
    }

    @Test
    fun `onSaveClicked with invalid form should not show confirmation dialog`() = runTest {
        val viewModel = createViewModel()
        viewModel.onSaveClicked()

        val state = viewModel.uiState.value
        assertFalse(state.showConfirmDialog)
    }

    @Test
    fun `dismissConfirmDialog should hide confirmation dialog`() = runTest {
        val book = TestBookFactory.createBook(id = "book-123")
        mockBooksRepository.bookResult = DomainResult.Success(book)

        val viewModel = createViewModel()
        viewModel.loadBook("book-123")
        advanceUntilIdle()

        viewModel.onTitleChange("Test Book")
        viewModel.onAuthorNameChange("J.R.R. Tolkien")
        viewModel.onCollectionNameChange("Fantasy Collection")
        viewModel.onBasePriceChange("10.0")
        viewModel.onSaveClicked()

        var state = viewModel.uiState.value
        assertTrue(state.showConfirmDialog)

        viewModel.dismissConfirmDialog()

        state = viewModel.uiState.value
        assertFalse(state.showConfirmDialog)
    }

    @Test
    fun `updateBook dismisses confirmation dialog before updating`() = runTest {
        val book = TestBookFactory.createBook(id = "book-123")
        mockBooksRepository.bookResult = DomainResult.Success(book)
        mockBooksRepository.updateBookResult = DomainResult.Success(
            TestBookFactory.createBook(id = "book-123")
        )

        val viewModel = createViewModel()
        viewModel.loadBook("book-123")
        advanceUntilIdle()

        viewModel.onTitleChange("Test Book")
        viewModel.onAuthorNameChange("J.R.R. Tolkien")
        viewModel.onCollectionNameChange("Fantasy Collection")
        viewModel.onBasePriceChange("10.0")
        viewModel.onSaveClicked()

        var state = viewModel.uiState.value
        assertTrue(state.showConfirmDialog)

        viewModel.updateBook()
        advanceUntilIdle()

        state = viewModel.uiState.value
        assertFalse(state.showConfirmDialog)
    }

    // ==================== FIELD VALIDATION ====================

    @Test
    fun `onTitleChange validates title field`() = runTest {
        val viewModel = createViewModel()
        viewModel.onTitleChange("   ")

        val state = viewModel.uiState.value
        assertEquals(R.string.error_title_required, state.titleError)
    }

    @Test
    fun `onBasePriceChange validates basePrice field`() = runTest {
        val viewModel = createViewModel()
        viewModel.onBasePriceChange("-10.0")

        val state = viewModel.uiState.value
        assertEquals(R.string.error_base_price_negative, state.basePriceError)
    }

    @Test
    fun `onIsbnChange validates isbn field`() = runTest {
        val viewModel = createViewModel()
        viewModel.onIsbnChange("invalid-isbn")

        val state = viewModel.uiState.value
        assertEquals(R.string.error_isbn_invalid, state.isbnError)
    }

    // ==================== ERROR HANDLING ====================

    @Test
    fun `updateBook with network error should set errorResId`() = runTest {
        val book = TestBookFactory.createBook(id = "book-123")
        mockBooksRepository.bookResult = DomainResult.Success(book)
        mockBooksRepository.updateBookResult = DomainResult.Error(DomainErrorType.NETWORK_ERROR)

        val viewModel = createViewModel()
        viewModel.loadBook("book-123")
        advanceUntilIdle()

        viewModel.onTitleChange("Test Book")
        viewModel.onAuthorNameChange("J.R.R. Tolkien")
        viewModel.onCollectionNameChange("Fantasy Collection")
        viewModel.onBasePriceChange("10.0")
        viewModel.updateBook()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.errorResId != null)
        assertFalse(state.sessionExpired)
    }

    @Test
    fun `onSessionExpiredHandled should clear sessionExpired flag`() = runTest {
        mockBooksRepository.bookResult = DomainResult.Error(DomainErrorType.UNAUTHORIZED)

        val viewModel = createViewModel()
        viewModel.loadBook("book-123")
        advanceUntilIdle()

        var state = viewModel.uiState.value
        assertTrue(state.sessionExpired)

        viewModel.onSessionExpiredHandled()

        state = viewModel.uiState.value
        assertFalse(state.sessionExpired)
    }
}
