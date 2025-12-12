package org.cescfe.book_publishing_app.data.author.repository

import java.io.IOException
import java.net.SocketTimeoutException
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.cescfe.book_publishing_app.data.author.remote.api.AuthorsApi
import org.cescfe.book_publishing_app.data.author.remote.dto.AuthorSummaryDTO
import org.cescfe.book_publishing_app.data.author.remote.dto.AuthorsResponse
import org.cescfe.book_publishing_app.data.shared.remote.dto.PaginationMeta
import org.cescfe.book_publishing_app.domain.shared.DomainErrorType
import org.cescfe.book_publishing_app.domain.shared.DomainResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class AuthorsRepositoryImplTest {

    private lateinit var mockAuthorsApi: MockAuthorsApi
    private lateinit var repository: AuthorsRepositoryImpl

    @Before
    fun setup() {
        mockAuthorsApi = MockAuthorsApi()
        repository = AuthorsRepositoryImpl(mockAuthorsApi)
    }

    // ==================== SUCCESS CASES ====================

    @Test
    fun `getAuthors with valid response should return Success with authors list`() = runTest {
        val authorDto = createAuthorDTO(
            id = "author-123",
            fullName = "J.R.R. Tolkien",
            pseudonym = "Tolkien",
            email = "tolkien@example.com"
        )
        mockAuthorsApi.successResponse = createAuthorsResponse(listOf(authorDto))

        val result = repository.getAuthors()

        assertTrue(result is DomainResult.Success)
        val success = result as DomainResult.Success
        assertEquals(1, success.data.size)
        assertEquals("author-123", success.data[0].id)
        assertEquals("J.R.R. Tolkien", success.data[0].fullName)
    }

    @Test
    fun `getAuthors with empty list should return Success with empty list`() = runTest {
        mockAuthorsApi.successResponse = createAuthorsResponse(emptyList())

        val result = repository.getAuthors()

        assertTrue(result is DomainResult.Success)
        val success = result as DomainResult.Success
        assertEquals(0, success.data.size)
    }

    @Test
    fun `getAuthors with multiple authors should return all authors`() = runTest {
        val authors = listOf(
            createAuthorDTO(id = "author-1", fullName = "Author One"),
            createAuthorDTO(id = "author-2", fullName = "Author Two"),
            createAuthorDTO(id = "author-3", fullName = "Author Three")
        )
        mockAuthorsApi.successResponse = createAuthorsResponse(authors)

        val result = repository.getAuthors()

        assertTrue(result is DomainResult.Success)
        val success = result as DomainResult.Success
        assertEquals(3, success.data.size)
    }

    // ==================== DTO TRANSFORMATION ====================

    @Test
    fun `getAuthors should transform DTO to domain model correctly`() = runTest {
        val authorDto = createAuthorDTO(
            id = "d7a3c6f9-9dc3-4fbf-b61a-83d59c81903e",
            fullName = "George Orwell",
            pseudonym = null,
            email = null
        )
        mockAuthorsApi.successResponse = createAuthorsResponse(listOf(authorDto))

        val result = repository.getAuthors()

        assertTrue(result is DomainResult.Success)
        val author = (result as DomainResult.Success).data[0]
        assertEquals("d7a3c6f9-9dc3-4fbf-b61a-83d59c81903e", author.id)
        assertEquals("George Orwell", author.fullName)
        assertEquals("", author.pseudonym)
        assertEquals("", author.email)
    }

    // ==================== ERROR HANDLING ====================

    @Test
    fun `getAuthors with SocketTimeoutException should return Timeout error`() = runTest {
        mockAuthorsApi.exception = SocketTimeoutException("Connection timed out")

        val result = repository.getAuthors()

        assertTrue(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assertEquals(DomainErrorType.TIMEOUT, error.type)
    }

    @Test
    fun `getAuthors with IOException should return NetworkError`() = runTest {
        mockAuthorsApi.exception = IOException("Network unavailable")

        val result = repository.getAuthors()

        assertTrue(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assertEquals(DomainErrorType.NETWORK_ERROR, error.type)
    }

    @Test
    fun `getAuthors with 401 HttpException should return Unauthorized error`() = runTest {
        mockAuthorsApi.httpException = createHttpException(401)

        val result = repository.getAuthors()

        assertTrue(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assertEquals(DomainErrorType.UNAUTHORIZED, error.type)
    }

    @Test
    fun `getAuthors with 500 HttpException should return ServerError`() = runTest {
        mockAuthorsApi.httpException = createHttpException(500)

        val result = repository.getAuthors()

        assertTrue(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assertEquals(DomainErrorType.SERVER_ERROR, error.type)
    }

    @Test
    fun `getAuthors with 503 HttpException should return ServerError`() = runTest {
        mockAuthorsApi.httpException = createHttpException(503)

        val result = repository.getAuthors()

        assertTrue(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assertEquals(DomainErrorType.SERVER_ERROR, error.type)
    }

    @Test
    fun `getAuthors with unknown exception should return Unknown error`() = runTest {
        mockAuthorsApi.exception = RuntimeException("Something unexpected")

        val result = repository.getAuthors()

        assertTrue(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assertEquals(DomainErrorType.UNKNOWN, error.type)
    }

    @Test
    fun `getAuthors with 404 HttpException should return Unknown error`() = runTest {
        mockAuthorsApi.httpException = createHttpException(404)

        val result = repository.getAuthors()

        assertTrue(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assertEquals(DomainErrorType.UNKNOWN, error.type)
    }

    // ==================== HELPERS ====================

    private fun createAuthorDTO(
        id: String = "default-id",
        fullName: String = "Default Author",
        pseudonym: String? = null,
        email: String? = null
    ) = AuthorSummaryDTO(
        id = id,
        fullName = fullName,
        pseudonym = pseudonym,
        email = email
    )

    private fun createAuthorsResponse(authors: List<AuthorSummaryDTO>) = AuthorsResponse(
        data = authors,
        meta = PaginationMeta(
            page = 1,
            limit = 20,
            total = authors.size,
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

class MockAuthorsApi : AuthorsApi {
    var successResponse: AuthorsResponse? = null
    var httpException: HttpException? = null
    var exception: Throwable? = null

    override suspend fun getAuthors(): AuthorsResponse = when {
        httpException != null -> throw httpException!!
        exception != null -> throw exception!!
        successResponse != null -> successResponse!!
        else -> throw RuntimeException("Mock not configured")
    }
}
