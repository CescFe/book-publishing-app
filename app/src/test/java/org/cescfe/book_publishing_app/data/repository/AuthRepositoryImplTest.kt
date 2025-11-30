package org.cescfe.book_publishing_app.data.repository

import java.io.IOException
import java.net.SocketTimeoutException
import kotlinx.coroutines.test.runTest
import org.cescfe.book_publishing_app.data.remote.api.AuthApi
import org.cescfe.book_publishing_app.data.remote.dto.LoginRequest
import org.cescfe.book_publishing_app.data.remote.dto.LoginResponse
import org.cescfe.book_publishing_app.domain.model.AuthResult
import org.cescfe.book_publishing_app.domain.model.ErrorType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

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
            access_token = "test_token",
            token_type = "Bearer",
            expires_in = 86400,
            scope = "read write delete",
            user_id = "user123"
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
        mockAuthApi.failureException = ApiException(401, "Bad credentials")

        val result = repository.login("wrong@example.com", "wrong")

        assertTrue(result is AuthResult.Error)
        val error = result as AuthResult.Error
        assertEquals(ErrorType.INVALID_CREDENTIALS, error.type)
        assertTrue(error.message.contains("Invalid credentials") || error.message.contains("Bad credentials"))
    }

    @Test
    fun `login with 500 should return ServerError`() = runTest {
        mockAuthApi.failureException = ApiException(500, "Internal server error")

        val result = repository.login("test@example.com", "password")

        assertTrue(result is AuthResult.Error)
        val error = result as AuthResult.Error
        assertEquals(ErrorType.SERVER_ERROR, error.type)
    }

    @Test
    fun `login with IOException should return NetworkError`() = runTest {
        mockAuthApi.failureException = IOException("Network error")

        val result = repository.login("test@example.com", "password")

        assertTrue(result is AuthResult.Error)
        val error = result as AuthResult.Error
        assertEquals(ErrorType.NETWORK_ERROR, error.type)
        assertTrue(error.message.contains("Network error"))
    }

    @Test
    fun `login with SocketTimeoutException should return Timeout error`() = runTest {
        mockAuthApi.failureException = SocketTimeoutException("Timeout")

        val result = repository.login("test@example.com", "password")

        assertTrue(result is AuthResult.Error)
        val error = result as AuthResult.Error
        assertEquals(ErrorType.TIMEOUT, error.type)
        assertTrue(error.message.contains("timeout"))
    }

    @Test
    fun `login with unknown exception should return Unknown error`() = runTest {
        mockAuthApi.failureException = RuntimeException("Unknown error")

        val result = repository.login("test@example.com", "password")

        assertTrue(result is AuthResult.Error)
        val error = result as AuthResult.Error
        assertEquals(ErrorType.UNKNOWN, error.type)
    }

    @Test
    fun `login should transform DTO to domain model correctly`() = runTest {
        val loginResponse = LoginResponse(
            access_token = "access_token_123",
            token_type = "Bearer",
            expires_in = 3600,
            scope = "read write",
            user_id = "user_456"
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
}

class MockAuthApi : AuthApi {
    var successResponse: LoginResponse? = null
    var failureException: Throwable? = null

    override suspend fun login(request: LoginRequest): Result<LoginResponse> = when {
        failureException != null -> Result.failure(failureException!!)
        successResponse != null -> Result.success(successResponse!!)
        else -> Result.failure(RuntimeException("Mock not configured"))
    }
}
