package org.cescfe.book_publishing_app.data.book.repository.helper

import org.cescfe.book_publishing_app.data.book.remote.api.BooksApi
import org.cescfe.book_publishing_app.data.book.remote.dto.BookDTO
import org.cescfe.book_publishing_app.data.book.remote.dto.BooksResponse
import retrofit2.HttpException

class MockBooksApi : BooksApi {
    var successResponse: BooksResponse? = null
    var httpException: HttpException? = null
    var exception: Throwable? = null

    var bookResponse: BookDTO? = null
    var bookHttpException: HttpException? = null
    var bookException: Throwable? = null

    override suspend fun getBooks(): BooksResponse = when {
        httpException != null -> throw httpException!!
        exception != null -> throw exception!!
        successResponse != null -> successResponse!!
        else -> throw RuntimeException("Mock not configured")
    }

    override suspend fun getBookById(id: String): BookDTO = when {
        bookHttpException != null -> throw bookHttpException!!
        bookException != null -> throw bookException!!
        bookResponse != null -> bookResponse!!
        else -> throw RuntimeException("Mock not configured for getBookById")
    }
}
