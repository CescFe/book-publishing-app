package org.cescfe.book_publishing_app.data.book.repository.helper

import org.cescfe.book_publishing_app.data.book.remote.api.BooksApi
import org.cescfe.book_publishing_app.data.book.remote.dto.BookDTO
import org.cescfe.book_publishing_app.data.book.remote.dto.BooksResponse
import org.cescfe.book_publishing_app.data.book.remote.dto.CreateBookRequestDTO
import org.cescfe.book_publishing_app.data.book.remote.dto.UpdateBookRequestDTO
import org.cescfe.book_publishing_app.data.shared.repository.helper.MockResult
import org.cescfe.book_publishing_app.data.shared.repository.helper.resolve

class MockBooksApi : BooksApi {
    var getBooksResult: MockResult<BooksResponse>? = null
    var getBookResult: MockResult<BookDTO>? = null
    var createBookResult: MockResult<BookDTO>? = null
    var updateBookResult: MockResult<BookDTO>? = null
    var deleteBookResult: MockResult<Unit>? = null

    var createBookRequest: CreateBookRequestDTO? = null
    var updateBookRequest: UpdateBookRequestDTO? = null
    var updateBookId: String? = null
    var deleteBookId: String? = null

    override suspend fun getBooks(): BooksResponse = getBooksResult.resolve()

    override suspend fun getBookById(id: String): BookDTO = getBookResult.resolve()

    override suspend fun createBook(request: CreateBookRequestDTO): BookDTO {
        createBookRequest = request
        return createBookResult.resolve()
    }

    override suspend fun updateBook(bookId: String, request: UpdateBookRequestDTO): BookDTO {
        updateBookId = bookId
        updateBookRequest = request
        return updateBookResult.resolve()
    }

    override suspend fun deleteBookById(bookId: String) {
        deleteBookId = bookId
        deleteBookResult.resolve()
    }
}
