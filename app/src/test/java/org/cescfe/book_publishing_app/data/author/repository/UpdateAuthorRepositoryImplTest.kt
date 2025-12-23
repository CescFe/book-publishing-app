package org.cescfe.book_publishing_app.data.author.repository

import java.io.IOException
import java.net.SocketTimeoutException
import kotlinx.coroutines.test.runTest
import org.cescfe.book_publishing_app.data.author.remote.dto.AuthorDTO
import org.cescfe.book_publishing_app.data.author.repository.helper.MockAuthorsApi
import org.cescfe.book_publishing_app.data.shared.repository.helper.TestHttpExceptionFactory
import org.cescfe.book_publishing_app.domain.author.model.UpdateAuthorRequest
import org.cescfe.book_publishing_app.domain.shared.DomainErrorType
import org.cescfe.book_publishing_app.domain.shared.DomainResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UpdateAuthorRepositoryImplTest {

    private lateinit var mockAuthorsApi: MockAuthorsApi
    private lateinit var repository: AuthorsRepositoryImpl

    @Before
    fun setup() {
        mockAuthorsApi = MockAuthorsApi()
        repository = AuthorsRepositoryImpl(mockAuthorsApi)
    }

    // ==================== UPDATE AUTHOR - SUCCESS CASE ====================

    @Test
    fun `updateAuthor with valid request should return Success with updated author`() = runTest {
        val authorId = "author-123"
        val request = UpdateAuthorRequest(
            fullName = "J.R.R. Tolkien",
            pseudonym = "Tolkien",
            biography = "English writer",
            email = "tolkien@example.com",
            website = "https://tolkien.com"
        )
        mockAuthorsApi.authorResponse = AuthorDTO(
            id = authorId,
            fullName = "J.R.R. Tolkien",
            pseudonym = "Tolkien",
            biography = "English writer",
            email = "tolkien@example.com",
            website = "https://tolkien.com"
        )

        val result = repository.updateAuthor(authorId, request)

        assertTrue(result is DomainResult.Success)
        val author = (result as DomainResult.Success).data
        assertEquals(authorId, author.id)
        assertEquals("J.R.R. Tolkien", author.fullName)
        assertEquals("Tolkien", author.pseudonym)
    }

    // ==================== UPDATE AUTHOR - REQUEST MAPPING ====================

    @Test
    fun `updateAuthor should send correct request to API`() = runTest {
        val authorId = "author-456"
        val request = UpdateAuthorRequest(
            fullName = "George Orwell",
            pseudonym = null,
            biography = "British novelist",
            email = null,
            website = null
        )
        mockAuthorsApi.authorResponse = AuthorDTO(
            id = authorId,
            fullName = "George Orwell"
        )

        repository.updateAuthor(authorId, request)

        assertEquals(authorId, mockAuthorsApi.updateAuthorId)
        val sentRequest = mockAuthorsApi.updateAuthorRequest
        assertEquals("George Orwell", sentRequest!!.fullName)
        assertEquals(null, sentRequest.pseudonym)
        assertEquals("British novelist", sentRequest.biography)
    }

    @Test
    fun `updateAuthor should send correct authorId to API`() = runTest {
        val authorId = "test-author-id"
        val request = UpdateAuthorRequest(fullName = "Test Author")
        mockAuthorsApi.authorResponse = AuthorDTO(
            id = authorId,
            fullName = "Test Author"
        )

        repository.updateAuthor(authorId, request)

        assertEquals(authorId, mockAuthorsApi.updateAuthorId)
    }

    // ==================== UPDATE AUTHOR - ERROR HANDLING ====================

    @Test
    fun `updateAuthor with SocketTimeoutException should return Timeout error`() = runTest {
        mockAuthorsApi.updateAuthorException = SocketTimeoutException("Connection timed out")

        val result = repository.updateAuthor("author-123", UpdateAuthorRequest(fullName = "Test"))

        assertTrue(result is DomainResult.Error)
        assertEquals(DomainErrorType.TIMEOUT, (result as DomainResult.Error).type)
    }

    @Test
    fun `updateAuthor with IOException should return NetworkError`() = runTest {
        mockAuthorsApi.updateAuthorException = IOException("Network unavailable")

        val result = repository.updateAuthor("author-123", UpdateAuthorRequest(fullName = "Test"))

        assertTrue(result is DomainResult.Error)
        assertEquals(DomainErrorType.NETWORK_ERROR, (result as DomainResult.Error).type)
    }

    @Test
    fun `updateAuthor with 400 HttpException should return Unknown error`() = runTest {
        mockAuthorsApi.updateAuthorHttpException = TestHttpExceptionFactory.create(400)

        val result = repository.updateAuthor("author-123", UpdateAuthorRequest(fullName = "Test"))

        assertTrue(result is DomainResult.Error)
        assertEquals(DomainErrorType.UNKNOWN, (result as DomainResult.Error).type)
    }

    @Test
    fun `updateAuthor with 401 HttpException should return Unauthorized error`() = runTest {
        mockAuthorsApi.updateAuthorHttpException = TestHttpExceptionFactory.create(401)

        val result = repository.updateAuthor("author-123", UpdateAuthorRequest(fullName = "Test"))

        assertTrue(result is DomainResult.Error)
        assertEquals(DomainErrorType.UNAUTHORIZED, (result as DomainResult.Error).type)
    }

    @Test
    fun `updateAuthor with 500 HttpException should return ServerError`() = runTest {
        mockAuthorsApi.updateAuthorHttpException = TestHttpExceptionFactory.create(500)

        val result = repository.updateAuthor("author-123", UpdateAuthorRequest(fullName = "Test"))

        assertTrue(result is DomainResult.Error)
        assertEquals(DomainErrorType.SERVER_ERROR, (result as DomainResult.Error).type)
    }
}
