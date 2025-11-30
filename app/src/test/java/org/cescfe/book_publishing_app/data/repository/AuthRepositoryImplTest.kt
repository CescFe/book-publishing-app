package org.cescfe.book_publishing_app.data.repository

import java.io.IOException
import java.net.SocketTimeoutException
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.cescfe.book_publishing_app.data.remote.api.AuthApi
import org.cescfe.book_publishing_app.data.remote.dto.LoginRequest
import org.cescfe.book_publishing_app.data.remote.dto.LoginResponse
import org.cescfe.book_publishing_app.domain.model.AuthResult
import org.cescfe.book_publishing_app.domain.model.ErrorType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class AuthRepositoryImplTest {

    private lateinit var mockAuthApi: MockAuthApi
    private lateinit var repository: AuthRepositoryImpl

    @Before
    fun setup() {
        mockAuthApi = MockAuthApi()
        repository = AuthRepositoryImpl(mockAuthApi)
    }

    @Test
    fun `login with valid credentials should return Success`() = runTest {
        mockAuthApi.successResponse = LoginResponse(
            accessToken = "test_token",
            tokenType = "Bearer",
            expiresIn = 86400,
            scope = "read write delete",
            userId = "user123"
        )

        val result = repository.login("test@example.com", "password123")

        assertTrue(result is AuthResult.Success)
        val success = result as AuthResult.Success
        assertEquals("test_token", success.token.accessToken)
        assertEquals("Bearer", success.token.tokenType)
        assertEquals(86400, success.token.expiresIn)
        assertEquals("read write delete", success.token.scope)
        assertEquals("user123", success.token.userId)
    }

    @Test
    fun `login with 401 should return InvalidCredentials error`() = runTest {
        mockAuthApi.httpException = createHttpException(401, "Bad credentials")

        val result = repository.login("wrong@example.com", "wrong")

        assertTrue(result is AuthResult.Error)
        val error = result as AuthResult.Error
        assertEquals(ErrorType.INVALID_CREDENTIALS, error.type)
        assertTrue(error.message.contains("Invalid credentials") || error.message.contains("Bad credentials"))
    }

    @Test
    fun `login with 500 should return ServerError`() = runTest {
        mockAuthApi.httpException = createHttpException(500, "Internal server error")

        val result = repository.login("test@example.com", "password")

        assertTrue(result is AuthResult.Error)
        val error = result as AuthResult.Error
        assertEquals(ErrorType.SERVER_ERROR, error.type)
    }

    @Test
    fun `login with IOException should return NetworkError`() = runTest {
        mockAuthApi.exception = IOException("Network error")

        val result = repository.login("test@example.com", "password")

        assertTrue(result is AuthResult.Error)
        val error = result as AuthResult.Error
        assertEquals(ErrorType.NETWORK_ERROR, error.type)
        assertTrue(error.message.contains("Network error"))
    }

    @Test
    fun `login with SocketTimeoutException should return Timeout error`() = runTest {
        mockAuthApi.exception = SocketTimeoutException("Timeout")

        val result = repository.login("test@example.com", "password")

        assertTrue(result is AuthResult.Error)
        val error = result as AuthResult.Error
        assertEquals(ErrorType.TIMEOUT, error.type)
        assertTrue(error.message.contains("timeout"))
    }

    @Test
    fun `login with unknown exception should return Unknown error`() = runTest {
        mockAuthApi.exception = RuntimeException("Unknown error")

        val result = repository.login("test@example.com", "password")

        assertTrue(result is AuthResult.Error)
        val error = result as AuthResult.Error
        assertEquals(ErrorType.UNKNOWN, error.type)
    }

    @Test
    fun `login should transform DTO to domain model correctly`() = runTest {
        val loginResponse = LoginResponse(
            accessToken = "access_token_123",
            tokenType = "Bearer",
            expiresIn = 3600,
            scope = "read write",
            userId = "user_456"
        )
        mockAuthApi.successResponse = loginResponse

        val result = repository.login("test@example.com", "password")

        assertTrue(result is AuthResult.Success)
        val success = result as AuthResult.Success
        assertEquals("access_token_123", success.token.accessToken)
        assertEquals("Bearer", success.token.tokenType)
        assertEquals(3600, success.token.expiresIn)
        assertEquals("read write", success.token.scope)
        assertEquals("user_456", success.token.userId)
    }

    @Test
    fun `login with 401 and error response body should parse error message`() = runTest {
        val errorBody =
            """{"status":401,"error":"Unauthorized","message":"Bad credentials","code":"BAD_CREDENTIALS"}"""
                .toResponseBody(
                    "application/json".toMediaType()
                )
        mockAuthApi.httpException = createHttpException(401, errorBody = errorBody)

        val result = repository.login("wrong@example.com", "wrong")

        assertTrue(result is AuthResult.Error)
        val error = result as AuthResult.Error
        assertEquals(ErrorType.INVALID_CREDENTIALS, error.type)
        assertEquals("Invalid credentials", error.message)
    }

    private fun createHttpException(
        code: Int,
        message: String? = null,
        errorBody: ResponseBody? = null
    ): HttpException {
        val responseBody = errorBody ?: (
            message
                ?: "Error"
            ).toResponseBody("application/json".toMediaType())
        val response = Response.error<Any>(code, responseBody)
        return HttpException(response)
    }
}

class MockAuthApi : AuthApi {
    var successResponse: LoginResponse? = null
    var httpException: HttpException? = null
    var exception: Throwable? = null

    override suspend fun login(request: LoginRequest): LoginResponse = when {
        httpException != null -> throw httpException!!
        exception != null -> throw exception!!
        successResponse != null -> successResponse!!
        else -> throw RuntimeException("Mock not configured")
    }
}
