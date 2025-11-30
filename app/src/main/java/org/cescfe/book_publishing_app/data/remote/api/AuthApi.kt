package org.cescfe.book_publishing_app.data.remote.api

import org.cescfe.book_publishing_app.data.remote.dto.LoginRequest
import org.cescfe.book_publishing_app.data.remote.dto.LoginResponse

interface AuthApi {
    suspend fun login(request: LoginRequest): Result<LoginResponse>
}