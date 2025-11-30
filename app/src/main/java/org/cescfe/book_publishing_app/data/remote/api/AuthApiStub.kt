package org.cescfe.book_publishing_app.data.remote.api

import kotlinx.coroutines.delay
import org.cescfe.book_publishing_app.data.remote.dto.LoginRequest
import org.cescfe.book_publishing_app.data.remote.dto.LoginResponse
import org.cescfe.book_publishing_app.data.repository.ApiException

class AuthApiStub : AuthApi {

    override suspend fun login(request: LoginRequest): Result<LoginResponse> {
        delay(1000)

        return when {
            request.username.isBlank() || request.password.isBlank() -> {
                Result.failure(
                    ApiException(
                        statusCode = 400,
                        message = "Username and password are required"
                    )
                )
            }
            request.username == "admin@example.com" && request.password == "admin123" -> {
                Result.success(
                    LoginResponse(
                        access_token = "stub_token_${System.currentTimeMillis()}",
                        token_type = "Bearer",
                        expires_in = 86400,
                        scope = "read write delete",
                        user_id = "stub_admin_user_id"
                    )
                )
            }
            request.username == "user@example.com" && request.password == "user123" -> {
                Result.success(
                    LoginResponse(
                        access_token = "stub_token_${System.currentTimeMillis()}",
                        token_type = "Bearer",
                        expires_in = 86400,
                        scope = "read",
                        user_id = "stub_user_id"
                    )
                )
            }
            else -> {
                Result.failure(
                    ApiException(
                        statusCode = 401,
                        message = "Bad credentials"
                    )
                )
            }
        }
    }
}
