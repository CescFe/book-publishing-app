package org.cescfe.book_publishing_app.domain.book.repository

import org.cescfe.book_publishing_app.domain.book.model.BookSummary
import org.cescfe.book_publishing_app.domain.book.model.BooksResult

interface BooksRepository {
    suspend fun getBooks(): BooksResult<List<BookSummary>>
}
