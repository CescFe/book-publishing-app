package org.cescfe.book_publishing_app.data.auth.remote.api

import org.cescfe.book_publishing_app.data.auth.remote.dto.LoginRequest
import org.cescfe.book_publishing_app.data.auth.remote.dto.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}
