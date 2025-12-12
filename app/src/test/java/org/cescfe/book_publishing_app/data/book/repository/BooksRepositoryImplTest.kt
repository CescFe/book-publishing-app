package org.cescfe.book_publishing_app.data.book.repository

import java.io.IOException
import java.net.SocketTimeoutException
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.cescfe.book_publishing_app.data.book.remote.api.BooksApi
import org.cescfe.book_publishing_app.data.book.remote.dto.BookSummaryDTO
import org.cescfe.book_publishing_app.data.book.remote.dto.BooksResponse
import org.cescfe.book_publishing_app.data.book.remote.dto.PaginationMeta
import org.cescfe.book_publishing_app.domain.book.model.BooksResult
import org.cescfe.book_publishing_app.domain.shared.DomainErrorType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class BooksRepositoryImplTest {

    private lateinit var mockBooksApi: MockBooksApi
    private lateinit var repository: BooksRepositoryImpl

    @Before
    fun setup() {
        mockBooksApi = MockBooksApi()
        repository = BooksRepositoryImpl(mockBooksApi)
    }

    // ==================== SUCCESS CASES ====================

    @Test
    fun `getBooks with valid response should return Success with books list`() = runTest {
        val bookDto = createBookDTO(
            id = "book-123",
            title = "The Lord of the Rings",
            authorId = "author-456",
            collectionId = "collection-789",
            basePrice = 29.99
        )
        mockBooksApi.successResponse = createBooksResponse(listOf(bookDto))

        val result = repository.getBooks()

        assertTrue(result is BooksResult.Success)
        val success = result as BooksResult.Success
        assertEquals(1, success.data.size)
        assertEquals("book-123", success.data[0].id)
        assertEquals("The Lord of the Rings", success.data[0].title)
    }

    @Test
    fun `getBooks with empty list should return Success with empty list`() = runTest {
        mockBooksApi.successResponse = createBooksResponse(emptyList())

        val result = repository.getBooks()

        assertTrue(result is BooksResult.Success)
        val success = result as BooksResult.Success
        assertEquals(0, success.data.size)
    }

    @Test
    fun `getBooks with multiple books should return all books`() = runTest {
        val books = listOf(
            createBookDTO(id = "book-1", title = "Book One"),
            createBookDTO(id = "book-2", title = "Book Two"),
            createBookDTO(id = "book-3", title = "Book Three")
        )
        mockBooksApi.successResponse = createBooksResponse(books)

        val result = repository.getBooks()

        assertTrue(result is BooksResult.Success)
        val success = result as BooksResult.Success
        assertEquals(3, success.data.size)
    }

    // ==================== DTO TRANSFORMATION ====================

    @Test
    fun `getBooks should transform DTO to domain model correctly`() = runTest {
        val bookDto = createBookDTO(
            id = "f59514f6-dbe8-41d0-a4ce-3e6cdf27290e",
            title = "The Lord of the Rings",
            authorId = "d7a3c6f9-9dc3-4fbf-b61a-83d59c81903e",
            collectionId = "8f5ef275-4987-47bc-8643-ff4e5efd6523",
            basePrice = 29.99,
            finalPrice = null,
            isbn = null
        )
        mockBooksApi.successResponse = createBooksResponse(listOf(bookDto))

        val result = repository.getBooks()

        assertTrue(result is BooksResult.Success)
        val book = (result as BooksResult.Success).data[0]
        assertEquals("f59514f6-dbe8-41d0-a4ce-3e6cdf27290e", book.id)
        assertEquals("The Lord of the Rings", book.title)
        assertEquals("d7a3c6f9-9dc3-4fbf-b61a-83d59c81903e", book.author)
        assertEquals("8f5ef275-4987-47bc-8643-ff4e5efd6523", book.collection)
        assertEquals(29.99, book.finalPrice, 0.001)
    }

    // ==================== ERROR HANDLING ====================

    @Test
    fun `getBooks with SocketTimeoutException should return Timeout error`() = runTest {
        mockBooksApi.exception = SocketTimeoutException("Connection timed out")

        val result = repository.getBooks()

        assertTrue(result is BooksResult.Error)
        val error = result as BooksResult.Error
        assertEquals(DomainErrorType.TIMEOUT, error.type)
        assertTrue(error.message.contains("timeout", ignoreCase = true))
    }

    @Test
    fun `getBooks with IOException should return NetworkError`() = runTest {
        mockBooksApi.exception = IOException("Network unavailable")

        val result = repository.getBooks()

        assertTrue(result is BooksResult.Error)
        val error = result as BooksResult.Error
        assertEquals(DomainErrorType.NETWORK_ERROR, error.type)
        assertTrue(error.message.contains("Network error", ignoreCase = true))
    }

    @Test
    fun `getBooks with 401 HttpException should return Unauthorized error`() = runTest {
        mockBooksApi.httpException = createHttpException(401)

        val result = repository.getBooks()

        assertTrue(result is BooksResult.Error)
        val error = result as BooksResult.Error
        assertEquals(DomainErrorType.UNAUTHORIZED, error.type)
        assertTrue(error.message.contains("Session expired", ignoreCase = true))
    }

    @Test
    fun `getBooks with 500 HttpException should return ServerError`() = runTest {
        mockBooksApi.httpException = createHttpException(500)

        val result = repository.getBooks()

        assertTrue(result is BooksResult.Error)
        val error = result as BooksResult.Error
        assertEquals(DomainErrorType.SERVER_ERROR, error.type)
        assertTrue(error.message.contains("Server error", ignoreCase = true))
    }

    @Test
    fun `getBooks with 503 HttpException should return ServerError`() = runTest {
        mockBooksApi.httpException = createHttpException(503)

        val result = repository.getBooks()

        assertTrue(result is BooksResult.Error)
        val error = result as BooksResult.Error
        assertEquals(DomainErrorType.SERVER_ERROR, error.type)
    }

    @Test
    fun `getBooks with unknown exception should return Unknown error`() = runTest {
        mockBooksApi.exception = RuntimeException("Something unexpected")

        val result = repository.getBooks()

        assertTrue(result is BooksResult.Error)
        val error = result as BooksResult.Error
        assertEquals(DomainErrorType.UNKNOWN, error.type)
    }

    @Test
    fun `getBooks with 404 HttpException should return Unknown error`() = runTest {
        mockBooksApi.httpException = createHttpException(404)

        val result = repository.getBooks()

        assertTrue(result is BooksResult.Error)
        val error = result as BooksResult.Error
        assertEquals(DomainErrorType.UNKNOWN, error.type)
    }

    // ==================== HELPERS ====================

    private fun createBookDTO(
        id: String = "default-id",
        title: String = "Default Title",
        authorId: String = "default-author",
        collectionId: String = "default-collection",
        basePrice: Double = 10.0,
        finalPrice: Double? = null,
        isbn: String? = null,
        status: String? = null
    ) = BookSummaryDTO(
        id = id,
        title = title,
        authorId = authorId,
        collectionId = collectionId,
        basePrice = basePrice,
        finalPrice = finalPrice,
        isbn = isbn,
        status = status
    )

    private fun createBooksResponse(books: List<BookSummaryDTO>) = BooksResponse(
        data = books,
        meta = PaginationMeta(
            page = 1,
            limit = 20,
            total = books.size,
            totalPages = 1
        )
    )

    private fun createHttpException(code: Int): HttpException {
        val responseBody = "Error".toResponseBody("application/json".toMediaType())
        val response = Response.error<Any>(code, responseBody)
        return HttpException(response)
    }
}

// ==================== MOCK ====================

class MockBooksApi : BooksApi {
    var successResponse: BooksResponse? = null
    var httpException: HttpException? = null
    var exception: Throwable? = null

    override suspend fun getBooks(): BooksResponse = when {
        httpException != null -> throw httpException!!
        exception != null -> throw exception!!
        successResponse != null -> successResponse!!
        else -> throw RuntimeException("Mock not configured")
    }
}
