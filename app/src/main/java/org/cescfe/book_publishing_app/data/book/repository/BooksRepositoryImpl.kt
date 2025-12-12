package org.cescfe.book_publishing_app.data.book.repository

import org.cescfe.book_publishing_app.data.book.remote.api.BooksApi
import org.cescfe.book_publishing_app.data.book.remote.dto.toDomain
import org.cescfe.book_publishing_app.data.shared.repository.RepositoryErrorHandler
import org.cescfe.book_publishing_app.domain.book.model.BookSummary
import org.cescfe.book_publishing_app.domain.book.repository.BooksRepository
import org.cescfe.book_publishing_app.domain.shared.DomainResult

class BooksRepositoryImpl(private val booksApi: BooksApi) : BooksRepository {

    override suspend fun getBooks(): DomainResult<List<BookSummary>> = try {
        val response = booksApi.getBooks()
        val books = response.data.map { it.toDomain() }
        DomainResult.Success(books)
    } catch (e: Exception) {
        RepositoryErrorHandler.handleException(e)
    }
}
