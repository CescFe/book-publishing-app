package org.cescfe.book_publishing_app.data.author.repository

import java.io.IOException
import java.net.SocketTimeoutException
import kotlinx.coroutines.test.runTest
import org.cescfe.book_publishing_app.data.author.remote.dto.AuthorDTO
import org.cescfe.book_publishing_app.data.author.repository.helper.MockAuthorsApi
import org.cescfe.book_publishing_app.data.shared.repository.helper.TestHttpExceptionFactory
import org.cescfe.book_publishing_app.domain.shared.DomainErrorType
import org.cescfe.book_publishing_app.domain.shared.DomainResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetAuthorRepositoryImplTest {

    private lateinit var mockAuthorsApi: MockAuthorsApi
    private lateinit var repository: AuthorsRepositoryImpl

    @Before
    fun setup() {
        mockAuthorsApi = MockAuthorsApi()
        repository = AuthorsRepositoryImpl(mockAuthorsApi)
    }

    // ==================== GET AUTHOR BY ID - DTO TRANSFORMATION ====================

    @Test
    fun `getAuthorById should transform DTO to domain model correctly`() = runTest {
        val authorDto = AuthorDTO(
            id = "d7a3c6f9-9dc3-4fbf-b61a-83d59c81903e",
            fullName = "Jane Austen",
            pseudonym = "A Lady",
            biography = "English novelist",
            email = "jane@example.com",
            website = null
        )
        mockAuthorsApi.authorResponse = authorDto

        val result = repository.getAuthorById("d7a3c6f9-9dc3-4fbf-b61a-83d59c81903e")

        assertTrue(result is DomainResult.Success)
        val author = (result as DomainResult.Success).data
        assertEquals(authorDto.id, author.id)
        assertEquals(authorDto.fullName, author.fullName)
        assertEquals(authorDto.pseudonym, author.pseudonym)
        assertEquals(authorDto.biography, author.biography)
        assertEquals(authorDto.email, author.email)
        assertEquals(authorDto.website, author.website)
    }

    // ==================== GET AUTHOR BY ID - ERROR HANDLING ====================

    @Test
    fun `getAuthorById with SocketTimeoutException should return Timeout error`() = runTest {
        mockAuthorsApi.authorException = SocketTimeoutException("Connection timed out")

        val result = repository.getAuthorById("author-123")

        assertTrue(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assertEquals(DomainErrorType.TIMEOUT, error.type)
    }

    @Test
    fun `getAuthorById with IOException should return NetworkError`() = runTest {
        mockAuthorsApi.authorException = IOException("Network unavailable")

        val result = repository.getAuthorById("author-123")

        assertTrue(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assertEquals(DomainErrorType.NETWORK_ERROR, error.type)
    }

    @Test
    fun `getAuthorById with 401 HttpException should return Unauthorized error`() = runTest {
        mockAuthorsApi.authorHttpException = TestHttpExceptionFactory.create(401)

        val result = repository.getAuthorById("author-123")

        assertTrue(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assertEquals(DomainErrorType.UNAUTHORIZED, error.type)
    }

    @Test
    fun `getAuthorById with 404 HttpException should return Unknown error`() = runTest {
        mockAuthorsApi.authorHttpException = TestHttpExceptionFactory.create(404)

        val result = repository.getAuthorById("non-existent")

        assertTrue(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assertEquals(DomainErrorType.UNKNOWN, error.type)
    }

    @Test
    fun `getAuthorById with 500 HttpException should return ServerError`() = runTest {
        mockAuthorsApi.authorHttpException = TestHttpExceptionFactory.create(500)

        val result = repository.getAuthorById("author-123")

        assertTrue(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assertEquals(DomainErrorType.SERVER_ERROR, error.type)
    }

    @Test
    fun `getAuthorById with 503 HttpException should return ServerError`() = runTest {
        mockAuthorsApi.authorHttpException = TestHttpExceptionFactory.create(503)

        val result = repository.getAuthorById("author-123")

        assertTrue(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assertEquals(DomainErrorType.SERVER_ERROR, error.type)
    }

    @Test
    fun `getAuthorById with unknown exception should return Unknown error`() = runTest {
        mockAuthorsApi.authorException = RuntimeException("Something unexpected")

        val result = repository.getAuthorById("author-123")

        assertTrue(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assertEquals(DomainErrorType.UNKNOWN, error.type)
    }
}
