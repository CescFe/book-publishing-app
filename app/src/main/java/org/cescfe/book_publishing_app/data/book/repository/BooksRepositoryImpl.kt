package org.cescfe.book_publishing_app.data.book.repository

import java.io.IOException
import java.net.SocketTimeoutException
import org.cescfe.book_publishing_app.data.book.remote.api.BooksApi
import org.cescfe.book_publishing_app.data.book.remote.dto.toDomain
import org.cescfe.book_publishing_app.domain.book.model.Book
import org.cescfe.book_publishing_app.domain.book.model.BooksResult
import org.cescfe.book_publishing_app.domain.book.repository.BooksRepository
import org.cescfe.book_publishing_app.domain.shared.DomainErrorType
import retrofit2.HttpException

class BooksRepositoryImpl(private val booksApi: BooksApi) : BooksRepository {

    override suspend fun getBooks(): BooksResult<List<Book>> = try {
        val response = booksApi.getBooks()
        val books = response.data.map { it.toDomain() }
        BooksResult.Success(books)
    } catch (e: HttpException) {
        mapHttpExceptionToError(e)
    } catch (_: SocketTimeoutException) {
        BooksResult.Error(
            DomainErrorType.TIMEOUT,
            "Request timeout. Please try again."
        )
    } catch (_: IOException) {
        BooksResult.Error(
            DomainErrorType.NETWORK_ERROR,
            "Network error. Please check your connection."
        )
    } catch (e: Exception) {
        BooksResult.Error(
            DomainErrorType.UNKNOWN,
            e.message ?: "An unexpected error occurred"
        )
    }

    private fun mapHttpExceptionToError(exception: HttpException): BooksResult.Error = when (exception.code()) {
        401 -> BooksResult.Error(
            DomainErrorType.UNAUTHORIZED,
            "Session expired. Please login again."
        )
        in 500..599 -> BooksResult.Error(
            DomainErrorType.SERVER_ERROR,
            "Server error. Please try again later."
        )
        else -> BooksResult.Error(
            DomainErrorType.UNKNOWN,
            "An error occurred"
        )
    }
}
