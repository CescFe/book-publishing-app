package org.cescfe.book_publishing_app.data.book.remote.api

import org.cescfe.book_publishing_app.data.book.remote.dto.BookDTO
import org.cescfe.book_publishing_app.data.book.remote.dto.BooksResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface BooksApi {
    @GET("api/v1/books")
    suspend fun getBooks(): BooksResponse

    @GET("api/v1/books/{id}")
    suspend fun getBookById(@Path("id") id: String): BookDTO
}
