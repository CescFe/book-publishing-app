package org.cescfe.book_publishing_app.data.shared.repository

import java.io.IOException
import java.net.SocketTimeoutException
import org.cescfe.book_publishing_app.data.shared.repository.helper.TestHttpExceptionFactory
import org.cescfe.book_publishing_app.domain.shared.DomainErrorType
import org.junit.Assert.assertEquals
import org.junit.Test

class RepositoryErrorHandlerTest {

    @Test
    fun `handleException should map 401 to UNAUTHORIZED`() {
        val exception = TestHttpExceptionFactory.create(401, "Unauthorized")
        val result = RepositoryErrorHandler.handleException(exception)

        assertEquals(DomainErrorType.UNAUTHORIZED, result.type)
    }

    @Test
    fun `handleException should map 403 to FORBIDDEN`() {
        val exception = TestHttpExceptionFactory.create(403, "Forbidden")
        val result = RepositoryErrorHandler.handleException(exception)

        assertEquals(DomainErrorType.FORBIDDEN, result.type)
    }

    @Test
    fun `handleException should map 500-599 to SERVER_ERROR`() {
        val exception500 = TestHttpExceptionFactory.create(500, "Internal Server Error")
        val result500 = RepositoryErrorHandler.handleException(exception500)
        assertEquals(DomainErrorType.SERVER_ERROR, result500.type)

        val exception503 = TestHttpExceptionFactory.create(503, "Service Unavailable")
        val result503 = RepositoryErrorHandler.handleException(exception503)
        assertEquals(DomainErrorType.SERVER_ERROR, result503.type)
    }

    @Test
    fun `handleException should map SocketTimeoutException to TIMEOUT`() {
        val exception = SocketTimeoutException("Timeout")
        val result = RepositoryErrorHandler.handleException(exception)

        assertEquals(DomainErrorType.TIMEOUT, result.type)
    }

    @Test
    fun `handleException should map IOException to NETWORK_ERROR`() {
        val exception = IOException("Network error")
        val result = RepositoryErrorHandler.handleException(exception)

        assertEquals(DomainErrorType.NETWORK_ERROR, result.type)
    }

    @Test
    fun `handleException should map unknown HttpException to UNKNOWN`() {
        val exception = TestHttpExceptionFactory.create(404, "Not Found")
        val result = RepositoryErrorHandler.handleException(exception)

        assertEquals(DomainErrorType.UNKNOWN, result.type)
    }

    @Test
    fun `handleException should map unknown exception to UNKNOWN`() {
        val exception = RuntimeException("Unknown error")
        val result = RepositoryErrorHandler.handleException(exception)

        assertEquals(DomainErrorType.UNKNOWN, result.type)
    }
}
