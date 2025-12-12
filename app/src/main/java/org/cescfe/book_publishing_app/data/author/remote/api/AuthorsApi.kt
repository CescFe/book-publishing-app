package org.cescfe.book_publishing_app.data.author.remote.api

import org.cescfe.book_publishing_app.data.author.remote.dto.AuthorsResponse
import retrofit2.http.GET

interface AuthorsApi {
    @GET("api/v1/authors")
    suspend fun getAuthors(): AuthorsResponse
}
