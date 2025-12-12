package org.cescfe.book_publishing_app.domain.collection.repository

import org.cescfe.book_publishing_app.domain.collection.model.CollectionSummary
import org.cescfe.book_publishing_app.domain.collection.model.CollectionsResult

interface CollectionsRepository {
    suspend fun getCollections(): CollectionsResult<List<CollectionSummary>>
}
