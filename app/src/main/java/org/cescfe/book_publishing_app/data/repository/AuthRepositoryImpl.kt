package org.cescfe.book_publishing_app.data.repository

import org.cescfe.book_publishing_app.data.remote.api.AuthApi
import org.cescfe.book_publishing_app.data.remote.dto.LoginRequest
import org.cescfe.book_publishing_app.data.remote.dto.LoginResponse
import org.cescfe.book_publishing_app.domain.model.AuthResult
import org.cescfe.book_publishing_app.domain.model.AuthToken
import org.cescfe.book_publishing_app.domain.model.ErrorType
import org.cescfe.book_publishing_app.domain.repository.AuthRepository
import java.io.IOException
import java.net.SocketTimeoutException

class AuthRepositoryImpl(
    private val authApi: AuthApi
) : AuthRepository {

    override suspend fun login(username: String, password: String): AuthResult {
        return try {
            val request = LoginRequest(username, password)
            val result = authApi.login(request)

            result.fold(
                onSuccess = { response ->
                    AuthResult.Success(response.toDomain())
                },
                onFailure = { throwable ->
                    mapExceptionToError(throwable)
                }
            )
        } catch (e: Exception) {
            mapExceptionToError(e)
        }
    }

    private fun mapExceptionToError(throwable: Throwable): AuthResult.Error {
        return when (throwable) {
            is SocketTimeoutException -> {
                AuthResult.Error(
                    ErrorType.TIMEOUT,
                    "Request timeout. Please try again."
                )
            }
            is IOException -> {
                AuthResult.Error(
                    ErrorType.NETWORK_ERROR,
                    "Network error. Please check your connection."
                )
            }
            is ApiException -> {
                when (throwable.statusCode) {
                    401 -> AuthResult.Error(
                        ErrorType.INVALID_CREDENTIALS,
                        throwable.message ?: "Invalid credentials"
                    )
                    in 500..599 -> AuthResult.Error(
                        ErrorType.SERVER_ERROR,
                        throwable.message ?: "Server error. Please try again later."
                    )
                    else -> AuthResult.Error(
                        ErrorType.UNKNOWN,
                        throwable.message ?: "An error occurred"
                    )
                }
            }
            else -> {
                AuthResult.Error(
                    ErrorType.UNKNOWN,
                    throwable.message ?: "An unexpected error occurred"
                )
            }
        }
    }
}

class ApiException(
    val statusCode: Int,
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause)

private fun LoginResponse.toDomain(): AuthToken {
    return AuthToken(
        accessToken = access_token,
        tokenType = token_type,
        expiresIn = expires_in,
        scope = scope,
        userId = user_id
    )
}