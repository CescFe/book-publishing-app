package org.cescfe.book_publishing_app.domain.shared

sealed class DomainResult<out T> {
    data class Success<T>(val data: T) : DomainResult<T>()
    data class Error(val type: DomainErrorType) : DomainResult<Nothing>()
}
