package org.cescfe.book_publishing_app.data.author.remote.api

import org.cescfe.book_publishing_app.data.author.remote.dto.AuthorDTO
import org.cescfe.book_publishing_app.data.author.remote.dto.AuthorsResponse
import org.cescfe.book_publishing_app.data.author.remote.dto.CreateAuthorRequestDTO
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthorsApi {
    @GET("api/v1/authors")
    suspend fun getAuthors(): AuthorsResponse

    @GET("api/v1/authors/{id}")
    suspend fun getAuthorById(@Path("id") id: String): AuthorDTO

    @POST("api/v1/authors")
    suspend fun createAuthor(@Body request: CreateAuthorRequestDTO): AuthorDTO

    @DELETE("api/v1/authors/{id}")
    suspend fun deleteAuthorById(@Path("id") authorId: String)
}
