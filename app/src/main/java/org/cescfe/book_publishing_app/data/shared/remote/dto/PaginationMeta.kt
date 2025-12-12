package org.cescfe.book_publishing_app.data.shared.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaginationMeta(
    val page: Int,
    val limit: Int,
    val total: Int,
    @SerialName("total_pages")
    val totalPages: Int
)
