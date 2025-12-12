package org.cescfe.book_publishing_app.domain.author.model

import org.cescfe.book_publishing_app.domain.shared.DomainErrorType

sealed class AuthorsResult<out T> {
    data class Success<T>(val data: T) : AuthorsResult<T>()
    data class Error(val type: DomainErrorType, val message: String) : AuthorsResult<Nothing>()
}
