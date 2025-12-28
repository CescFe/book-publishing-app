package org.cescfe.book_publishing_app.data.book.repository

import java.io.IOException
import java.net.SocketTimeoutException
import kotlinx.coroutines.test.runTest
import org.cescfe.book_publishing_app.data.book.repository.helper.MockBooksApi
import org.cescfe.book_publishing_app.data.shared.repository.helper.MockResult
import org.cescfe.book_publishing_app.data.shared.repository.helper.TestHttpExceptionFactory
import org.cescfe.book_publishing_app.domain.shared.DomainErrorType
import org.cescfe.book_publishing_app.domain.shared.DomainResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DeleteBookRepositoryImplTest {
    private lateinit var mockBooksApi: MockBooksApi
    private lateinit var repository: BooksRepositoryImpl

    @Before
    fun setup() {
        mockBooksApi = MockBooksApi()
        repository = BooksRepositoryImpl(mockBooksApi)
    }

    // ==================== DELETE BOOK BY ID - SUCCESS CASE ====================

    @Test
    fun `deleteBookById with successful deletion should return Success with Unit`() = runTest {
        mockBooksApi.deleteBookResult = MockResult.Success(Unit)

        val result = repository.deleteBookById("book-123")

        assertTrue(result is DomainResult.Success)
        val success = result as DomainResult.Success
        assertEquals(Unit, success.data)
        assertEquals("book-123", mockBooksApi.deleteBookId)
    }

    // ==================== DELETE BOOK BY ID - API CALL VERIFICATION ====================

    @Test
    fun `deleteBookById should call API with correct book ID`() = runTest {
        mockBooksApi.deleteBookResult = MockResult.Success(Unit)
        val expectedBookId = "test-book-id-456"

        repository.deleteBookById(expectedBookId)

        assertEquals(expectedBookId, mockBooksApi.deleteBookId)
    }

    // ==================== DELETE BOOK BY ID - ERROR HANDLING ====================

    @Test
    fun `deleteBookById with SocketTimeoutException should return Timeout error`() = runTest {
        mockBooksApi.deleteBookResult = MockResult.Error(
            SocketTimeoutException("Connection timed out")
        )

        val result = repository.deleteBookById("book-123")

        assertTrue(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assertEquals(DomainErrorType.TIMEOUT, error.type)
    }

    @Test
    fun `deleteBookById with IOException should return NetworkError`() = runTest {
        mockBooksApi.deleteBookResult = MockResult.Error(
            IOException("Network unavailable")
        )

        val result = repository.deleteBookById("book-123")

        assertTrue(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assertEquals(DomainErrorType.NETWORK_ERROR, error.type)
    }

    @Test
    fun `deleteBookById with 401 HttpException should return Unauthorized error`() = runTest {
        mockBooksApi.deleteBookResult = MockResult.Error(
            TestHttpExceptionFactory.create(401)
        )

        val result = repository.deleteBookById("book-123")

        assertTrue(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assertEquals(DomainErrorType.UNAUTHORIZED, error.type)
    }

    @Test
    fun `deleteBookById with 404 HttpException should return Unknown error`() = runTest {
        mockBooksApi.deleteBookResult = MockResult.Error(
            TestHttpExceptionFactory.create(404)
        )

        val result = repository.deleteBookById("non-existent")

        assertTrue(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assertEquals(DomainErrorType.UNKNOWN, error.type)
    }

    @Test
    fun `deleteBookById with 500 HttpException should return ServerError`() = runTest {
        mockBooksApi.deleteBookResult = MockResult.Error(
            TestHttpExceptionFactory.create(500)
        )

        val result = repository.deleteBookById("book-123")

        assertTrue(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assertEquals(DomainErrorType.SERVER_ERROR, error.type)
    }
}
