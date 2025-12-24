package org.cescfe.book_publishing_app.data.book.repository

import java.io.IOException
import java.net.SocketTimeoutException
import kotlinx.coroutines.test.runTest
import org.cescfe.book_publishing_app.data.book.remote.dto.AuthorRefDTO
import org.cescfe.book_publishing_app.data.book.remote.dto.BookDTO
import org.cescfe.book_publishing_app.data.book.remote.dto.CollectionRefDTO
import org.cescfe.book_publishing_app.data.book.repository.helper.MockBooksApi
import org.cescfe.book_publishing_app.data.shared.repository.helper.TestHttpExceptionFactory
import org.cescfe.book_publishing_app.domain.book.model.enums.Status
import org.cescfe.book_publishing_app.domain.shared.DomainErrorType
import org.cescfe.book_publishing_app.domain.shared.DomainResult
import org.cescfe.book_publishing_app.domain.shared.enums.Genre
import org.cescfe.book_publishing_app.domain.shared.enums.Language
import org.cescfe.book_publishing_app.domain.shared.enums.ReadingLevel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetBookRepositoryImplTest {

    private lateinit var mockBooksApi: MockBooksApi
    private lateinit var repository: BooksRepositoryImpl

    @Before
    fun setup() {
        mockBooksApi = MockBooksApi()
        repository = BooksRepositoryImpl(mockBooksApi)
    }

    // ==================== SUCCESS CASES ====================

    @Test
    fun `getBookById should transform DTO to domain model correctly`() = runTest {
        val bookDto = createBookDTO(
            id = "1b871121-881e-44cf-b7b8-a2e9954ce6d6",
            title = "Harry Potter and the Deathly Hallows",
            authorName = "Joanne Rowling",
            collectionName = "Young Wizards",
            readingLevel = "YOUNG_ADULT",
            primaryLanguage = "ENGLISH",
            secondaryLanguages = listOf("SPANISH", "CATALAN"),
            primaryGenre = "FANTASY",
            secondaryGenres = listOf("ADVENTURE", "MYSTERY"),
            basePrice = 29.99,
            finalPrice = 31.19,
            isbn = "9780747591054",
            publicationDate = "2007-07-21",
            pageCount = 607,
            description = "Harry, Ron, and Hermione hunt for Horcruxes",
            status = Status.DRAFT
        )
        mockBooksApi.bookResponse = bookDto

        val result = repository.getBookById("1b871121-881e-44cf-b7b8-a2e9954ce6d6")

        assertTrue(result is DomainResult.Success)
        val book = (result as DomainResult.Success).data
        assertEquals(bookDto.id, book.id)
        assertEquals(bookDto.title, book.title)
        assertEquals(bookDto.basePrice, book.basePrice, 0.001)
        assertEquals(bookDto.author.name, book.authorName)
        assertEquals(bookDto.collection.name, book.collectionName)
        assertEquals(ReadingLevel.YOUNG_ADULT, book.readingLevel)
        assertEquals(Language.ENGLISH, book.primaryLanguage)
        assertEquals(2, book.secondaryLanguages.size)
        assertEquals(Language.SPANISH, book.secondaryLanguages[0])
        assertEquals(Language.CATALAN, book.secondaryLanguages[1])
        assertEquals(Genre.FANTASY, book.primaryGenre)
        assertEquals(2, book.secondaryGenres.size)
        assertEquals(Genre.ADVENTURE, book.secondaryGenres[0])
        assertEquals(Genre.MYSTERY, book.secondaryGenres[1])
        assertEquals(bookDto.vatRate, book.vatRate, 0.001)
        assertEquals(bookDto.finalPrice, book.finalPrice, 0.001)
        assertEquals(bookDto.isbn, book.isbn)
        assertEquals(bookDto.publicationDate, book.publicationDate)
        assertEquals(bookDto.pageCount, book.pageCount)
        assertEquals(bookDto.description, book.description)
        assertEquals(Status.DRAFT, book.status)
    }

    @Test
    fun `getBookById with nullable fields should handle correctly`() = runTest {
        val bookDto = createBookDTO(
            id = "1",
            title = "Test Book",
            readingLevel = null,
            primaryLanguage = null,
            secondaryLanguages = emptyList(),
            primaryGenre = null,
            secondaryGenres = emptyList(),
            isbn = null,
            publicationDate = null,
            pageCount = null,
            description = null
        )
        mockBooksApi.bookResponse = bookDto

        val result = repository.getBookById("1")

        assertTrue(result is DomainResult.Success)
        val book = (result as DomainResult.Success).data
        assertNull(book.readingLevel)
        assertNull(book.primaryLanguage)
        assertEquals(0, book.secondaryLanguages.size)
        assertNull(book.primaryGenre)
        assertEquals(0, book.secondaryGenres.size)
        assertNull(book.isbn)
        assertNull(book.publicationDate)
        assertNull(book.pageCount)
        assertNull(book.description)
    }

    // ==================== ERROR HANDLING ====================

    @Test
    fun `getBookById with SocketTimeoutException should return Timeout error`() = runTest {
        mockBooksApi.bookException = SocketTimeoutException("Connection timed out")

        val result = repository.getBookById("1")

        assertTrue(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assertEquals(DomainErrorType.TIMEOUT, error.type)
    }

    @Test
    fun `getBookById with IOException should return NetworkError`() = runTest {
        mockBooksApi.bookException = IOException("Network unavailable")

        val result = repository.getBookById("1")

        assertTrue(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assertEquals(DomainErrorType.NETWORK_ERROR, error.type)
    }

    @Test
    fun `getBookById with 401 HttpException should return Unauthorized error`() = runTest {
        mockBooksApi.bookHttpException = TestHttpExceptionFactory.create(401)

        val result = repository.getBookById("1")

        assertTrue(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assertEquals(DomainErrorType.UNAUTHORIZED, error.type)
    }

    @Test
    fun `getBookById with 404 HttpException should return Unknown error`() = runTest {
        mockBooksApi.bookHttpException = TestHttpExceptionFactory.create(404)

        val result = repository.getBookById("non-existent")

        assertTrue(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assertEquals(DomainErrorType.UNKNOWN, error.type)
    }

    @Test
    fun `getBookById with 500 HttpException should return ServerError`() = runTest {
        mockBooksApi.bookHttpException = TestHttpExceptionFactory.create(500)

        val result = repository.getBookById("1")

        assertTrue(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assertEquals(DomainErrorType.SERVER_ERROR, error.type)
    }

    @Test
    fun `getBookById with 503 HttpException should return ServerError`() = runTest {
        mockBooksApi.bookHttpException = TestHttpExceptionFactory.create(503)

        val result = repository.getBookById("1")

        assertTrue(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assertEquals(DomainErrorType.SERVER_ERROR, error.type)
    }

    @Test
    fun `getBookById with unknown exception should return Unknown error`() = runTest {
        mockBooksApi.bookException = RuntimeException("Something unexpected")

        val result = repository.getBookById("1")

        assertTrue(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assertEquals(DomainErrorType.UNKNOWN, error.type)
    }

    // ==================== HELPERS ====================

    private fun createBookDTO(
        id: String = "default-id",
        title: String = "Default Title",
        authorId: String = "default-author",
        authorName: String = "Default Author",
        collectionId: String = "default-collection",
        collectionName: String = "Default Collection",
        readingLevel: String? = "ADULT",
        primaryLanguage: String? = "ENGLISH",
        secondaryLanguages: List<String> = emptyList(),
        primaryGenre: String? = "FANTASY",
        secondaryGenres: List<String> = emptyList(),
        basePrice: Double = 10.0,
        vatRate: Double = 0.04,
        finalPrice: Double = 10.04,
        isbn: String? = "1234567890",
        publicationDate: String? = "2024-01-01",
        pageCount: Int? = 100,
        description: String? = "Default description",
        status: Status = Status.DRAFT
    ) = BookDTO(
        id = id,
        title = title,
        basePrice = basePrice,
        author = AuthorRefDTO(id = authorId, name = authorName),
        collection = CollectionRefDTO(id = collectionId, name = collectionName),
        readingLevel = readingLevel,
        primaryLanguage = primaryLanguage,
        secondaryLanguages = secondaryLanguages,
        primaryGenre = primaryGenre,
        secondaryGenres = secondaryGenres,
        vatRate = vatRate,
        finalPrice = finalPrice,
        isbn = isbn,
        publicationDate = publicationDate,
        pageCount = pageCount,
        description = description,
        status = status
    )
}
