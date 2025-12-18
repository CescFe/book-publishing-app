package org.cescfe.book_publishing_app.data.author.repository

import java.io.IOException
import java.net.SocketTimeoutException
import kotlinx.coroutines.test.runTest
import org.cescfe.book_publishing_app.data.author.remote.dto.AuthorDTO
import org.cescfe.book_publishing_app.data.author.repository.helper.MockAuthorsApi
import org.cescfe.book_publishing_app.data.shared.repository.helper.TestHttpExceptionFactory
import org.cescfe.book_publishing_app.domain.author.model.CreateAuthorRequest
import org.cescfe.book_publishing_app.domain.shared.DomainErrorType
import org.cescfe.book_publishing_app.domain.shared.DomainResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CreateAuthorRepositoryImplTest {

    private lateinit var mockAuthorsApi: MockAuthorsApi
    private lateinit var repository: AuthorsRepositoryImpl

    @Before
    fun setup() {
        mockAuthorsApi = MockAuthorsApi()
        repository = AuthorsRepositoryImpl(mockAuthorsApi)
    }

    // ==================== CREATE AUTHOR - SUCCESS CASE ====================

    @Test
    fun `createAuthor with valid request should return Success with created author`() = runTest {
        val request = CreateAuthorRequest(
            fullName = "J.R.R. Tolkien",
            pseudonym = "Tolkien",
            biography = "English writer",
            email = "tolkien@example.com",
            website = "https://tolkien.com"
        )
        mockAuthorsApi.authorResponse = AuthorDTO(
            id = "new-author-123",
            fullName = "J.R.R. Tolkien",
            pseudonym = "Tolkien",
            biography = "English writer",
            email = "tolkien@example.com",
            website = "https://tolkien.com"
        )

        val result = repository.createAuthor(request)

        assertTrue(result is DomainResult.Success)
        val author = (result as DomainResult.Success).data
        assertEquals("new-author-123", author.id)
        assertEquals("J.R.R. Tolkien", author.fullName)
    }

    // ==================== CREATE AUTHOR - REQUEST MAPPING ====================

    @Test
    fun `createAuthor should send correct request to API`() = runTest {
        val request = CreateAuthorRequest(
            fullName = "George Orwell",
            pseudonym = null,
            biography = "British novelist",
            email = null,
            website = null
        )
        mockAuthorsApi.authorResponse = AuthorDTO(
            id = "author-456",
            fullName = "George Orwell"
        )

        repository.createAuthor(request)

        val sentRequest = mockAuthorsApi.createAuthorRequest
        assertEquals("George Orwell", sentRequest!!.fullName)
        assertEquals(null, sentRequest.pseudonym)
        assertEquals("British novelist", sentRequest.biography)
    }

    // ==================== CREATE AUTHOR - ERROR HANDLING ====================

    @Test
    fun `createAuthor with SocketTimeoutException should return Timeout error`() = runTest {
        mockAuthorsApi.authorException = SocketTimeoutException("Connection timed out")

        val result = repository.createAuthor(CreateAuthorRequest(fullName = "Test"))

        assertTrue(result is DomainResult.Error)
        assertEquals(DomainErrorType.TIMEOUT, (result as DomainResult.Error).type)
    }

    @Test
    fun `createAuthor with IOException should return NetworkError`() = runTest {
        mockAuthorsApi.authorException = IOException("Network unavailable")

        val result = repository.createAuthor(CreateAuthorRequest(fullName = "Test"))

        assertTrue(result is DomainResult.Error)
        assertEquals(DomainErrorType.NETWORK_ERROR, (result as DomainResult.Error).type)
    }

    @Test
    fun `createAuthor with 401 HttpException should return Unauthorized error`() = runTest {
        mockAuthorsApi.authorHttpException = TestHttpExceptionFactory.create(401)

        val result = repository.createAuthor(CreateAuthorRequest(fullName = "Test"))

        assertTrue(result is DomainResult.Error)
        assertEquals(DomainErrorType.UNAUTHORIZED, (result as DomainResult.Error).type)
    }

    @Test
    fun `createAuthor with 500 HttpException should return ServerError`() = runTest {
        mockAuthorsApi.authorHttpException = TestHttpExceptionFactory.create(500)

        val result = repository.createAuthor(CreateAuthorRequest(fullName = "Test"))

        assertTrue(result is DomainResult.Error)
        assertEquals(DomainErrorType.SERVER_ERROR, (result as DomainResult.Error).type)
    }
}
