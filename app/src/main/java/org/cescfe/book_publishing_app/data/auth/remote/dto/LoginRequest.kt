package org.cescfe.book_publishing_app.data.auth.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val username: String, val password: String)
