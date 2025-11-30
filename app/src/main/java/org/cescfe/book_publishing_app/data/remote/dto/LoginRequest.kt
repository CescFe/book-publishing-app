package org.cescfe.book_publishing_app.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val username: String, val password: String)
