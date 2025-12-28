package org.cescfe.book_publishing_app.data.shared.repository.helper

import retrofit2.HttpException

sealed class MockResult<out T> {
    data class Success<T>(val value: T) : MockResult<T>()
    data class HttpError(val exception: HttpException) : MockResult<Nothing>()
    data class Error(val throwable: Throwable) : MockResult<Nothing>()
}

fun <T> MockResult<T>?.resolve(): T = when (this) {
    is MockResult.Success -> value
    is MockResult.HttpError -> throw exception
    is MockResult.Error -> throw throwable
    null -> throw RuntimeException("Mock not configured")
}
