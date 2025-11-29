package org.cescfe.book_publishing_app.domain.model

sealed class AuthResult {
    data class Success(val token: AuthToken) : AuthResult()

    data class Error(val type: ErrorType, val message: String) : AuthResult() {
        constructor(message: String) : this(ErrorType.UNKNOWN, message)
    }
}

enum class ErrorType {
    INVALID_CREDENTIALS,

    NETWORK_ERROR,

    SERVER_ERROR,

    TIMEOUT,

    UNKNOWN
}
