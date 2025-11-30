package org.cescfe.book_publishing_app.data.book.remote.api

import org.cescfe.book_publishing_app.data.book.remote.dto.BooksResponse
import retrofit2.http.GET

interface BooksApi {
    @GET("api/v1/books")
    suspend fun getBooks(): BooksResponse
}
