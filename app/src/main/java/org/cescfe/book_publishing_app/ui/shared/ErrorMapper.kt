package org.cescfe.book_publishing_app.ui.shared

import android.content.Context
import org.cescfe.book_publishing_app.R
import org.cescfe.book_publishing_app.domain.shared.DomainErrorType

fun DomainErrorType.toMessage(context: Context): String = when (this) {
    DomainErrorType.TIMEOUT -> context.getString(R.string.error_timeout)
    DomainErrorType.NETWORK_ERROR -> context.getString(R.string.error_network)
    DomainErrorType.UNAUTHORIZED -> context.getString(R.string.error_unauthorized)
    DomainErrorType.SERVER_ERROR -> context.getString(R.string.error_server)
    DomainErrorType.UNKNOWN -> context.getString(R.string.error_unknown)
}
