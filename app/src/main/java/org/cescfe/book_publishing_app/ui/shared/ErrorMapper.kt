package org.cescfe.book_publishing_app.ui.shared

import androidx.annotation.StringRes
import org.cescfe.book_publishing_app.R
import org.cescfe.book_publishing_app.domain.shared.DomainErrorType

@StringRes
fun DomainErrorType.toStringResId(): Int = when (this) {
    DomainErrorType.TIMEOUT -> R.string.error_timeout
    DomainErrorType.NETWORK_ERROR -> R.string.error_network
    DomainErrorType.UNAUTHORIZED -> R.string.error_unauthorized
    DomainErrorType.FORBIDDEN -> R.string.error_forbidden
    DomainErrorType.SERVER_ERROR -> R.string.error_server
    DomainErrorType.UNKNOWN -> R.string.error_unknown
}
