package org.cescfe.book_publishing_app.domain.book.model

import org.cescfe.book_publishing_app.domain.shared.DomainErrorType

sealed class BooksResult<out T> {
    data class Success<T>(val data: T) : BooksResult<T>()
    data class Error(val type: DomainErrorType, val message: String) : BooksResult<Nothing>()
}
