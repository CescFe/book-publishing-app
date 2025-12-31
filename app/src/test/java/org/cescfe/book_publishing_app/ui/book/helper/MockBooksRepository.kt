package org.cescfe.book_publishing_app.ui.book.helper

import org.cescfe.book_publishing_app.domain.book.model.Book
import org.cescfe.book_publishing_app.domain.book.model.BookSummary
import org.cescfe.book_publishing_app.domain.book.model.CreateBookRequest
import org.cescfe.book_publishing_app.domain.book.model.UpdateBookRequest
import org.cescfe.book_publishing_app.domain.book.repository.BooksRepository
import org.cescfe.book_publishing_app.domain.shared.DomainErrorType
import org.cescfe.book_publishing_app.domain.shared.DomainResult

class MockBooksRepository : BooksRepository {
    var result: DomainResult<List<BookSummary>> = DomainResult.Success(emptyList())
    var bookResult: DomainResult<Book> = DomainResult.Error(DomainErrorType.UNKNOWN)
    var createBookResult: DomainResult<Book> = DomainResult.Error(DomainErrorType.UNKNOWN)
    var deleteResult: DomainResult<Unit> = DomainResult.Success(Unit)

    override suspend fun getBooks(): DomainResult<List<BookSummary>> = result
    override suspend fun getBookById(bookId: String): DomainResult<Book> = bookResult
    override suspend fun createBook(request: CreateBookRequest): DomainResult<Book> = createBookResult
    override suspend fun updateBook(bookId: String, request: UpdateBookRequest): DomainResult<Book> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteBookById(bookId: String): DomainResult<Unit> = deleteResult
}
