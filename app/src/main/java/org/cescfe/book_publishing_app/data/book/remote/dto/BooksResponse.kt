package org.cescfe.book_publishing_app.data.book.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BooksResponse(val data: List<BookSummaryDTO>, val meta: PaginationMeta)

@Serializable
data class PaginationMeta(
    val page: Int,
    val limit: Int,
    val total: Int,
    @SerialName("total_pages")
    val totalPages: Int
)
