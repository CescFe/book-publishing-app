package org.cescfe.book_publishing_app.data.collection.remote.api

import org.cescfe.book_publishing_app.data.collection.remote.dto.CollectionsResponse
import retrofit2.http.GET

interface CollectionsApi {
    @GET("api/v1/collections")
    suspend fun getCollections(): CollectionsResponse
}
