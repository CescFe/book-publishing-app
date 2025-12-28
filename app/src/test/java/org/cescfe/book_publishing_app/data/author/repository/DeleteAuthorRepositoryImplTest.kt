package org.cescfe.book_publishing_app.data.author.repository

import java.io.IOException
import java.net.SocketTimeoutException
import kotlinx.coroutines.test.runTest
import org.cescfe.book_publishing_app.data.author.repository.helper.MockAuthorsApi
import org.cescfe.book_publishing_app.data.shared.repository.helper.TestHttpExceptionFactory
import org.cescfe.book_publishing_app.domain.shared.DomainErrorType
import org.cescfe.book_publishing_app.domain.shared.DomainResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DeleteAuthorRepositoryImplTest {
    private lateinit var mockAuthorsApi: MockAuthorsApi
    private lateinit var repository: AuthorsRepositoryImpl

    @Before
    fun setup() {
        mockAuthorsApi = MockAuthorsApi()
        repository = AuthorsRepositoryImpl(mockAuthorsApi)
    }

    // ==================== DELETE AUTHOR BY ID - SUCCESS CASE ====================

    @Test
    fun `deleteAuthorById with successful deletion should return Success with Unit`() = runTest {
        mockAuthorsApi.deleteSuccess = true

        val result = repository.deleteAuthorById("author-123")

        assertTrue(result is DomainResult.Success)
        val success = result as DomainResult.Success
        assertEquals(Unit, success.data)
        assertEquals("author-123", mockAuthorsApi.deleteAuthorId)
    }

    // ==================== DELETE AUTHOR BY ID - API CALL VERIFICATION ====================

    @Test
    fun `deleteAuthorById should call API with correct author ID`() = runTest {
        mockAuthorsApi.deleteSuccess = true
        val expectedAuthorId = "test-author-id-456"

        repository.deleteAuthorById(expectedAuthorId)

        assertEquals(expectedAuthorId, mockAuthorsApi.deleteAuthorId)
    }

    // ==================== DELETE AUTHOR BY ID - ERROR HANDLING ====================

    @Test
    fun `deleteAuthorById with SocketTimeoutException should return Timeout error`() = runTest {
        mockAuthorsApi.exception = SocketTimeoutException("Connection timed out")

        val result = repository.deleteAuthorById("author-123")

        assertTrue(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assertEquals(DomainErrorType.TIMEOUT, error.type)
    }

    @Test
    fun `deleteAuthorById with IOException should return NetworkError`() = runTest {
        mockAuthorsApi.exception = IOException("Network unavailable")

        val result = repository.deleteAuthorById("author-123")

        assertTrue(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assertEquals(DomainErrorType.NETWORK_ERROR, error.type)
    }

    @Test
    fun `deleteAuthorById with 401 HttpException should return Unauthorized error`() = runTest {
        mockAuthorsApi.httpException = TestHttpExceptionFactory.create(401)

        val result = repository.deleteAuthorById("author-123")

        assertTrue(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assertEquals(DomainErrorType.UNAUTHORIZED, error.type)
    }

    @Test
    fun `deleteAuthorById with 404 HttpException should return Unknown error`() = runTest {
        mockAuthorsApi.httpException = TestHttpExceptionFactory.create(404)

        val result = repository.deleteAuthorById("non-existent")

        assertTrue(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assertEquals(DomainErrorType.UNKNOWN, error.type)
    }

    @Test
    fun `deleteAuthorById with 500 HttpException should return ServerError`() = runTest {
        mockAuthorsApi.httpException = TestHttpExceptionFactory.create(500)

        val result = repository.deleteAuthorById("author-123")

        assertTrue(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assertEquals(DomainErrorType.SERVER_ERROR, error.type)
    }
}
