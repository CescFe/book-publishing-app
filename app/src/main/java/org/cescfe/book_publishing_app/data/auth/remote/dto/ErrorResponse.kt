package org.cescfe.book_publishing_app.data.auth.remote.dto

data class ErrorResponse(val status: Int, val error: String, val message: String, val code: String? = null)
