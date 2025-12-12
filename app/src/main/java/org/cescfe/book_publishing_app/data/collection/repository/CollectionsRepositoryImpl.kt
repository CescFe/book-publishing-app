package org.cescfe.book_publishing_app.data.collection.repository

import org.cescfe.book_publishing_app.data.collection.remote.api.CollectionsApi
import org.cescfe.book_publishing_app.data.collection.remote.dto.toDomain
import org.cescfe.book_publishing_app.data.shared.repository.RepositoryErrorHandler
import org.cescfe.book_publishing_app.domain.collection.model.CollectionSummary
import org.cescfe.book_publishing_app.domain.collection.repository.CollectionsRepository
import org.cescfe.book_publishing_app.domain.shared.DomainResult

class CollectionsRepositoryImpl(private val collectionsApi: CollectionsApi) : CollectionsRepository {
    override suspend fun getCollections(): DomainResult<List<CollectionSummary>> = try {
        val response = collectionsApi.getCollections()
        val collections = response.data.map { it.toDomain() }
        DomainResult.Success(collections)
    } catch (e: Exception) {
        RepositoryErrorHandler.handleException(e)
    }
}
