package org.cescfe.book_publishing_app.ui.collection.helper

import org.cescfe.book_publishing_app.domain.collection.model.CollectionSummary
import org.cescfe.book_publishing_app.domain.collection.repository.CollectionsRepository
import org.cescfe.book_publishing_app.domain.shared.DomainResult

class MockCollectionsRepository : CollectionsRepository {
    var collectionsResult: DomainResult<List<CollectionSummary>> = DomainResult.Success(emptyList())

    override suspend fun getCollections(): DomainResult<List<CollectionSummary>> = collectionsResult
}
