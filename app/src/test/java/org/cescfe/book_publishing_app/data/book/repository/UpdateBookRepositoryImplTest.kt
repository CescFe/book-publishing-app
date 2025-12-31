package org.cescfe.book_publishing_app.data.book.repository

import java.io.IOException
import java.net.SocketTimeoutException
import kotlinx.coroutines.test.runTest
import org.cescfe.book_publishing_app.data.book.remote.dto.AuthorRefDTO
import org.cescfe.book_publishing_app.data.book.remote.dto.BookDTO
import org.cescfe.book_publishing_app.data.book.remote.dto.CollectionRefDTO
import org.cescfe.book_publishing_app.data.book.repository.helper.MockBooksApi
import org.cescfe.book_publishing_app.data.shared.repository.helper.MockResult
import org.cescfe.book_publishing_app.data.shared.repository.helper.TestHttpExceptionFactory
import org.cescfe.book_publishing_app.domain.book.model.UpdateBookRequest
import org.cescfe.book_publishing_app.domain.book.model.enums.Status
import org.cescfe.book_publishing_app.domain.shared.DomainErrorType
import org.cescfe.book_publishing_app.domain.shared.DomainResult
import org.cescfe.book_publishing_app.domain.shared.enums.Genre
import org.cescfe.book_publishing_app.domain.shared.enums.Language
import org.cescfe.book_publishing_app.domain.shared.enums.ReadingLevel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UpdateBookRepositoryImplTest {

    private lateinit var mockBooksApi: MockBooksApi
    private lateinit var repository: BooksRepositoryImpl

    @Before
    fun setup() {
        mockBooksApi = MockBooksApi()
        repository = BooksRepositoryImpl(mockBooksApi)
    }

    // ==================== UPDATE BOOK - SUCCESS CASE ====================

    @Test
    fun `updateBook with valid request should return Success with updated book`() = runTest {
        val bookId = "book-123"
        val request = UpdateBookRequest(
            title = "The Lord of the Rings",
            authorId = "author-123",
            collectionId = "collection-456",
            basePrice = 29.99,
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
        val bookDto = BookDTO(
            id = bookId,
            title = "The Lord of the Rings",
            basePrice = 29.99,
            author = AuthorRefDTO(
                id = "author-123",
                name = "J.R.R. Tolkien"
            ),
            collection = CollectionRefDTO(
                id = "collection-456",
                name = "Fantasy Collection"
            ),
            readingLevel = "ADULT",
            primaryLanguage = "ENGLISH",
            secondaryLanguages = listOf("SPANISH", "CATALAN"),
            primaryGenre = "FANTASY",
            secondaryGenres = listOf("ADVENTURE"),
            vatRate = 0.04,
            finalPrice = 31.19,
            isbn = "978-0-544-00001-0",
            publicationDate = "1954-07-29",
            pageCount = 1178,
            description = "Epic fantasy novel",
            status = Status.PUBLISHED
        )

        mockBooksApi.updateBookResult = MockResult.Success(bookDto)

        val result = repository.updateBook(bookId, request)

        assertTrue(result is DomainResult.Success)
        val book = (result as DomainResult.Success).data
        assertEquals(bookId, book.id)
        assertEquals("The Lord of the Rings", book.title)
        assertEquals(29.99, book.basePrice, 0.001)
        assertEquals("J.R.R. Tolkien", book.authorName)
        assertEquals("Fantasy Collection", book.collectionName)
        assertEquals(ReadingLevel.ADULT, book.readingLevel)
        assertEquals(Language.ENGLISH, book.primaryLanguage)
        assertEquals(2, book.secondaryLanguages.size)
        assertEquals(Genre.FANTASY, book.primaryGenre)
        assertEquals(1, book.secondaryGenres.size)
        assertEquals(0.04, book.vatRate, 0.001)
        assertEquals("978-0-544-00001-0", book.isbn)
        assertEquals("1954-07-29", book.publicationDate)
        assertEquals(1178, book.pageCount)
        assertEquals("Epic fantasy novel", book.description)
        assertEquals(Status.PUBLISHED, book.status)
    }

    // ==================== UPDATE BOOK - REQUEST MAPPING ====================

    @Test
    fun `updateBook should send correct request to API`() = runTest {
        val bookId = "book-456"
        val request = UpdateBookRequest(
            title = "1984",
            authorId = "author-789",
            collectionId = "collection-012",
            basePrice = 15.50,
            readingLevel = null,
            primaryLanguage = null,
            secondaryLanguages = emptyList(),
            primaryGenre = null,
            secondaryGenres = emptyList(),
            vatRate = null,
            isbn = null,
            publicationDate = null,
            pageCount = null,
            description = null,
            status = null
        )
        val bookDto = BookDTO(
            id = bookId,
            title = "1984",
            basePrice = 15.50,
            author = AuthorRefDTO(
                id = "author-789",
                name = "George Orwell"
            ),
            collection = CollectionRefDTO(
                id = "collection-012",
                name = "Classics"
            ),
            vatRate = 0.04,
            finalPrice = 16.12,
            status = Status.DRAFT
        )

        mockBooksApi.updateBookResult = MockResult.Success(bookDto)

        repository.updateBook(bookId, request)

        assertEquals(bookId, mockBooksApi.updateBookId)
        val sentRequest = mockBooksApi.updateBookRequest
        assertEquals("1984", sentRequest!!.title)
        assertEquals("author-789", sentRequest.authorId)
        assertEquals("collection-012", sentRequest.collectionId)
        assertEquals(15.50, sentRequest.basePrice, 0.001)
        assertEquals(null, sentRequest.readingLevel)
        assertEquals(null, sentRequest.primaryLanguage)
        assertEquals(emptyList<String>(), sentRequest.secondaryLanguages)
        assertEquals(null, sentRequest.primaryGenre)
        assertEquals(emptyList<String>(), sentRequest.secondaryGenres)
        assertEquals(null, sentRequest.vatRate)
        assertEquals(null, sentRequest.isbn)
        assertEquals(null, sentRequest.publicationDate)
        assertEquals(null, sentRequest.pageCount)
        assertEquals(null, sentRequest.description)
        assertEquals(null, sentRequest.status)
    }

    @Test
    fun `updateBook should send correct bookId to API`() = runTest {
        val bookId = "test-book-id"
        val request = UpdateBookRequest(
            title = "Test Book",
            authorId = "author-1",
            collectionId = "collection-1",
            basePrice = 10.0
        )
        val bookDto = BookDTO(
            id = bookId,
            title = "Test Book",
            basePrice = 10.0,
            author = AuthorRefDTO(
                id = "author-1",
                name = "Test Author"
            ),
            collection = CollectionRefDTO(
                id = "collection-1",
                name = "Test Collection"
            ),
            vatRate = 0.04,
            finalPrice = 10.4,
            status = Status.DRAFT
        )

        mockBooksApi.updateBookResult = MockResult.Success(bookDto)

        repository.updateBook(bookId, request)

        assertEquals(bookId, mockBooksApi.updateBookId)
    }

    // ==================== UPDATE BOOK - ERROR HANDLING ====================

    @Test
    fun `updateBook with SocketTimeoutException should return Timeout error`() = runTest {
        mockBooksApi.updateBookResult = MockResult.Error(
            SocketTimeoutException("Connection timed out")
        )

        val result = repository.updateBook(
            "book-123",
            UpdateBookRequest(
                title = "Test Book",
                authorId = "author-1",
                collectionId = "collection-1",
                basePrice = 10.0
            )
        )

        assertTrue(result is DomainResult.Error)
        assertEquals(DomainErrorType.TIMEOUT, (result as DomainResult.Error).type)
    }

    @Test
    fun `updateBook with IOException should return NetworkError`() = runTest {
        mockBooksApi.updateBookResult = MockResult.Error(
            IOException("Network unavailable")
        )

        val result = repository.updateBook(
            "book-123",
            UpdateBookRequest(
                title = "Test Book",
                authorId = "author-1",
                collectionId = "collection-1",
                basePrice = 10.0
            )
        )

        assertTrue(result is DomainResult.Error)
        assertEquals(DomainErrorType.NETWORK_ERROR, (result as DomainResult.Error).type)
    }

    @Test
    fun `updateBook with 400 HttpException should return Unknown error`() = runTest {
        mockBooksApi.updateBookResult = MockResult.Error(
            TestHttpExceptionFactory.create(400)
        )

        val result = repository.updateBook(
            "book-123",
            UpdateBookRequest(
                title = "Test Book",
                authorId = "author-1",
                collectionId = "collection-1",
                basePrice = 10.0
            )
        )

        assertTrue(result is DomainResult.Error)
        assertEquals(DomainErrorType.UNKNOWN, (result as DomainResult.Error).type)
    }

    @Test
    fun `updateBook with 401 HttpException should return Unauthorized error`() = runTest {
        mockBooksApi.updateBookResult = MockResult.Error(
            TestHttpExceptionFactory.create(401)
        )

        val result = repository.updateBook(
            "book-123",
            UpdateBookRequest(
                title = "Test Book",
                authorId = "author-1",
                collectionId = "collection-1",
                basePrice = 10.0
            )
        )

        assertTrue(result is DomainResult.Error)
        assertEquals(DomainErrorType.UNAUTHORIZED, (result as DomainResult.Error).type)
    }

    @Test
    fun `updateBook with 500 HttpException should return ServerError`() = runTest {
        mockBooksApi.updateBookResult = MockResult.Error(
            TestHttpExceptionFactory.create(500)
        )

        val result = repository.updateBook(
            "book-123",
            UpdateBookRequest(
                title = "Test Book",
                authorId = "author-1",
                collectionId = "collection-1",
                basePrice = 10.0
            )
        )

        assertTrue(result is DomainResult.Error)
        assertEquals(DomainErrorType.SERVER_ERROR, (result as DomainResult.Error).type)
    }
}
