package org.cescfe.book_publishing_app.domain.book.repository

import org.cescfe.book_publishing_app.domain.book.model.Book

interface BooksRepository {
    suspend fun getBooks(): Result<List<Book>>
}
