package org.cescfe.book_publishing_app.ui.book.helper

import org.cescfe.book_publishing_app.domain.book.model.Book
import org.cescfe.book_publishing_app.domain.book.model.BookSummary
import org.cescfe.book_publishing_app.domain.book.repository.BooksRepository
import org.cescfe.book_publishing_app.domain.shared.DomainErrorType
import org.cescfe.book_publishing_app.domain.shared.DomainResult

class MockBooksRepository : BooksRepository {
    var result: DomainResult<List<BookSummary>> = DomainResult.Success(emptyList())
    var bookResult: DomainResult<Book> = DomainResult.Error(DomainErrorType.UNKNOWN)

    override suspend fun getBooks(): DomainResult<List<BookSummary>> = result
    override suspend fun getBookById(bookId: String): DomainResult<Book> = bookResult
}
