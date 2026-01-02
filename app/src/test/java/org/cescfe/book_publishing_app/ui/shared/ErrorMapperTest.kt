package org.cescfe.book_publishing_app.ui.shared

import org.cescfe.book_publishing_app.R
import org.cescfe.book_publishing_app.domain.shared.DomainErrorType
import org.junit.Assert.assertEquals
import org.junit.Test

class ErrorMapperTest {

    @Test
    fun `toStringResId should return correct resource for TIMEOUT`() {
        assertEquals(R.string.error_timeout, DomainErrorType.TIMEOUT.toStringResId())
    }

    @Test
    fun `toStringResId should return correct resource for NETWORK_ERROR`() {
        assertEquals(R.string.error_network, DomainErrorType.NETWORK_ERROR.toStringResId())
    }

    @Test
    fun `toStringResId should return correct resource for UNAUTHORIZED`() {
        assertEquals(R.string.error_unauthorized, DomainErrorType.UNAUTHORIZED.toStringResId())
    }

    @Test
    fun `toStringResId should return correct resource for FORBIDDEN`() {
        assertEquals(R.string.error_forbidden, DomainErrorType.FORBIDDEN.toStringResId())
    }

    @Test
    fun `toStringResId should return correct resource for SERVER_ERROR`() {
        assertEquals(R.string.error_server, DomainErrorType.SERVER_ERROR.toStringResId())
    }

    @Test
    fun `toStringResId should return correct resource for UNKNOWN`() {
        assertEquals(R.string.error_unknown, DomainErrorType.UNKNOWN.toStringResId())
    }
}
