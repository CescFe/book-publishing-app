package org.cescfe.book_publishing_app.domain.collection.model

import org.cescfe.book_publishing_app.domain.shared.DomainErrorType

sealed class CollectionsResult<out T> {
    data class Success<T>(val data: T) : CollectionsResult<T>()
    data class Error(val type: DomainErrorType, val message: String) : CollectionsResult<Nothing>()
}
