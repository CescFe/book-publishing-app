package org.cescfe.book_publishing_app.data.auth.repository

import java.io.IOException
import java.net.SocketTimeoutException
import kotlinx.serialization.json.Json
import okhttp3.ResponseBody
import org.cescfe.book_publishing_app.data.auth.TokenManager
import org.cescfe.book_publishing_app.data.auth.remote.api.AuthApi
import org.cescfe.book_publishing_app.data.auth.remote.dto.ErrorResponse
import org.cescfe.book_publishing_app.data.auth.remote.dto.LoginRequest
import org.cescfe.book_publishing_app.data.auth.remote.dto.LoginResponse
import org.cescfe.book_publishing_app.domain.auth.model.AuthResult
import org.cescfe.book_publishing_app.domain.auth.model.AuthToken
import org.cescfe.book_publishing_app.domain.auth.model.ErrorType
import org.cescfe.book_publishing_app.domain.auth.repository.AuthRepository
import retrofit2.HttpException

class AuthRepositoryImpl(private val authApi: AuthApi) : AuthRepository {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    override suspend fun login(username: String, password: String): AuthResult = try {
        val request = LoginRequest(username, password)
        val response = authApi.login(request)

        TokenManager.saveToken(response.accessToken)

        AuthResult.Success(response.toDomain())
    } catch (e: HttpException) {
        mapHttpExceptionToError(e)
    } catch (_: SocketTimeoutException) {
        AuthResult.Error(
            ErrorType.TIMEOUT,
            "Request timeout. Please try again."
        )
    } catch (_: IOException) {
        AuthResult.Error(
            ErrorType.NETWORK_ERROR,
            "Network error. Please check your connection."
        )
    } catch (e: Exception) {
        AuthResult.Error(
            ErrorType.UNKNOWN,
            e.message ?: "An unexpected error occurred"
        )
    }

    private fun mapHttpExceptionToError(exception: HttpException): AuthResult.Error {
        val errorBody = exception.response()?.errorBody()
        val errorResponse = parseErrorResponse(errorBody)

        return when (exception.code()) {
            401 -> AuthResult.Error(
                ErrorType.INVALID_CREDENTIALS,
                errorResponse?.message ?: "Invalid credentials"
            )
            in 500..599 -> AuthResult.Error(
                ErrorType.SERVER_ERROR,
                errorResponse?.message ?: "Server error. Please try again later."
            )
            else -> AuthResult.Error(
                ErrorType.UNKNOWN,
                errorResponse?.message ?: "An error occurred"
            )
        }
    }

    private fun parseErrorResponse(errorBody: ResponseBody?): ErrorResponse? = try {
        errorBody?.string()?.let { jsonString ->
            json.decodeFromString<ErrorResponse>(jsonString)
        }
    } catch (_: Exception) {
        null
    }
}

private fun LoginResponse.toDomain(): AuthToken = AuthToken(
    accessToken = accessToken,
    tokenType = tokenType,
    expiresIn = expiresIn,
    scope = scope,
    userId = userId
)
