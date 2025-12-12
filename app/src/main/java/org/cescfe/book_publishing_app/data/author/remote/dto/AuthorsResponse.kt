package org.cescfe.book_publishing_app.data.author.remote.dto

import kotlinx.serialization.Serializable
import org.cescfe.book_publishing_app.data.shared.remote.dto.PaginationMeta

@Serializable
data class AuthorsResponse(val data: List<AuthorSummaryDTO>, val meta: PaginationMeta)
