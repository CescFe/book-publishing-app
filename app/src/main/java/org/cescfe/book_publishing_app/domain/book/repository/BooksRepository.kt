package org.cescfe.book_publishing_app.domain.book.repository

import org.cescfe.book_publishing_app.domain.book.model.Book
import org.cescfe.book_publishing_app.domain.book.model.BookSummary
import org.cescfe.book_publishing_app.domain.book.model.CreateBookRequest
import org.cescfe.book_publishing_app.domain.shared.DomainResult

interface BooksRepository {
    suspend fun getBooks(): DomainResult<List<BookSummary>>
    suspend fun getBookById(bookId: String): DomainResult<Book>
    suspend fun createBook(request: CreateBookRequest): DomainResult<Book>
}
