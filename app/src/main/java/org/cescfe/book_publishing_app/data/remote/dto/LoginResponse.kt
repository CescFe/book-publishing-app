package org.cescfe.book_publishing_app.data.remote.dto

data class LoginResponse(
    val access_token: String,
    val token_type: String,
    val expires_in: Int,
    val scope: String,
    val user_id: String
)
