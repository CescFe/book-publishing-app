package org.cescfe.book_publishing_app.data.shared.repository

import java.io.IOException
import java.net.SocketTimeoutException
import org.cescfe.book_publishing_app.domain.shared.DomainErrorType
import org.cescfe.book_publishing_app.domain.shared.DomainResult
import retrofit2.HttpException

object RepositoryErrorHandler {

    fun handleException(e: Throwable): DomainResult.Error = when (e) {
        is HttpException -> mapHttpExceptionToError(e)
        is SocketTimeoutException -> DomainResult.Error(DomainErrorType.TIMEOUT)
        is IOException -> DomainResult.Error(DomainErrorType.NETWORK_ERROR)
        else -> DomainResult.Error(DomainErrorType.UNKNOWN)
    }

    private fun mapHttpExceptionToError(ex: HttpException): DomainResult.Error = when (ex.code()) {
        401 -> DomainResult.Error(DomainErrorType.UNAUTHORIZED)
        403 -> DomainResult.Error(DomainErrorType.FORBIDDEN)
        in 500..599 -> DomainResult.Error(DomainErrorType.SERVER_ERROR)
        else -> DomainResult.Error(DomainErrorType.UNKNOWN)
    }
}
