package org.cescfe.book_publishing_app.domain.collection.model

sealed class CollectionsResult<out T> {
    data class Success<T>(val data: T) : CollectionsResult<T>()
    data class Error(val type: CollectionsErrorType, val message: String) : CollectionsResult<Nothing>()
}

enum class CollectionsErrorType {
    NETWORK_ERROR,
    TIMEOUT,
    SERVER_ERROR,
    UNAUTHORIZED,
    UNKNOWN
}
