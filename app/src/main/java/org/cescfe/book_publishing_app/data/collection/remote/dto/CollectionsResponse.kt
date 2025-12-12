package org.cescfe.book_publishing_app.data.collection.remote.dto

import kotlinx.serialization.Serializable
import org.cescfe.book_publishing_app.data.shared.remote.dto.PaginationMeta

@Serializable
data class CollectionsResponse(val data: List<CollectionSummaryDTO>, val meta: PaginationMeta)
