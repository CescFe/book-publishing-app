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
import org.cescfe.book_publishing_app.domain.book.model.CreateBookRequest
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

class CreateBookRepositoryImplTest {

    private lateinit var mockBooksApi: MockBooksApi
    private lateinit var repository: BooksRepositoryImpl

    @Before
    fun setup() {
        mockBooksApi = MockBooksApi()
        repository = BooksRepositoryImpl(mockBooksApi)
    }

    // ==================== CREATE BOOK - SUCCESS CASE ====================

    @Test
    fun `createBook with valid request should return Success with created book`() = runTest {
        val request = CreateBookRequest(
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
            id = "new-book-123",
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

        mockBooksApi.createBookResult = MockResult.Success(bookDto)

        val result = repository.createBook(request)

        assertTrue(result is DomainResult.Success)
        val book = (result as DomainResult.Success).data
        assertEquals("new-book-123", book.id)
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

    // ==================== CREATE BOOK - REQUEST MAPPING ====================

    @Test
    fun `createBook should send correct request to API`() = runTest {
        val request = CreateBookRequest(
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
            id = "book-456",
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

        mockBooksApi.createBookResult = MockResult.Success(bookDto)

        repository.createBook(request)

        val sentRequest = mockBooksApi.createBookRequest
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

    // ==================== CREATE BOOK - ERROR HANDLING ====================

    @Test
    fun `createBook with SocketTimeoutException should return Timeout error`() = runTest {
        mockBooksApi.createBookResult = MockResult.Error(
            SocketTimeoutException("Connection timed out")
        )

        val result = repository.createBook(
            CreateBookRequest(
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
    fun `createBook with IOException should return NetworkError`() = runTest {
        mockBooksApi.createBookResult = MockResult.Error(
            IOException("Network unavailable")
        )

        val result = repository.createBook(
            CreateBookRequest(
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
    fun `createBook with 401 HttpException should return Unauthorized error`() = runTest {
        mockBooksApi.createBookResult = MockResult.Error(
            TestHttpExceptionFactory.create(401)
        )

        val result = repository.createBook(
            CreateBookRequest(
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
    fun `createBook with 500 HttpException should return ServerError`() = runTest {
        mockBooksApi.createBookResult = MockResult.Error(
            TestHttpExceptionFactory.create(500)
        )

        val result = repository.createBook(
            CreateBookRequest(
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
