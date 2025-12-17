package org.cescfe.book_publishing_app.data.author.remote.api

import org.cescfe.book_publishing_app.data.author.remote.dto.AuthorDTO
import org.cescfe.book_publishing_app.data.author.remote.dto.AuthorsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface AuthorsApi {
    @GET("api/v1/authors")
    suspend fun getAuthors(): AuthorsResponse

    @GET("api/v1/authors/{id}")
    suspend fun getAuthorById(@Path("id") id: String): AuthorDTO
}
