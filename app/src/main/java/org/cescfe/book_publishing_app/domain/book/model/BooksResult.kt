package org.cescfe.book_publishing_app.domain.book.model

sealed class BooksResult<out T> {
    data class Success<T>(val data: T) : BooksResult<T>()
    data class Error(val type: BooksErrorType, val message: String) : BooksResult<Nothing>()
}

enum class BooksErrorType {
    NETWORK_ERROR,
    TIMEOUT,
    SERVER_ERROR,
    UNAUTHORIZED,
    UNKNOWN
}
