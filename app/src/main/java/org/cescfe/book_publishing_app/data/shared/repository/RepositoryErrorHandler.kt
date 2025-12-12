package org.cescfe.book_publishing_app.data.shared.repository

import org.cescfe.book_publishing_app.domain.shared.DomainErrorType
import org.cescfe.book_publishing_app.domain.shared.DomainResult
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

object RepositoryErrorHandler {

    fun handleException(e: Throwable): DomainResult.Error = when (e) {
        is HttpException -> mapHttpExceptionToError(e)
        is SocketTimeoutException -> DomainResult.Error(
            DomainErrorType.TIMEOUT,
            "Request timeout. Please try again."
        )
        is IOException -> DomainResult.Error(
            DomainErrorType.NETWORK_ERROR,
            "Network error. Please check your connection."
        )
        else -> DomainResult.Error(
            DomainErrorType.UNKNOWN,
            e.message ?: "An unexpected error occurred"
        )
    }

    private fun mapHttpExceptionToError(ex: HttpException): DomainResult.Error = when (ex.code()) {
        401 -> DomainResult.Error(
            DomainErrorType.UNAUTHORIZED,
            "Session expired. Please login again."
        )
        in 500..599 -> DomainResult.Error(
            DomainErrorType.SERVER_ERROR,
            "Server error. Please try again later."
        )
        else -> DomainResult.Error(
            DomainErrorType.UNKNOWN,
            "An error occurred"
        )
    }
}
