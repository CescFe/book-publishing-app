package org.cescfe.book_publishing_app.data.book.remote.dto

import kotlinx.serialization.Serializable
import org.cescfe.book_publishing_app.data.shared.remote.dto.PaginationMeta

@Serializable
data class BooksResponse(val data: List<BookSummaryDTO>, val meta: PaginationMeta)
