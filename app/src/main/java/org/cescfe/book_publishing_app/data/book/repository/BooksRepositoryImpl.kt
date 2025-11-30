package org.cescfe.book_publishing_app.data.book.repository

import org.cescfe.book_publishing_app.data.book.remote.api.BooksApi
import org.cescfe.book_publishing_app.data.book.remote.dto.toDomain
import org.cescfe.book_publishing_app.domain.book.model.Book
import org.cescfe.book_publishing_app.domain.book.model.BooksErrorType
import org.cescfe.book_publishing_app.domain.book.model.BooksResult
import org.cescfe.book_publishing_app.domain.book.repository.BooksRepository
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class BooksRepositoryImpl(
    private val booksApi: BooksApi
) : BooksRepository {

    override suspend fun getBooks(): BooksResult<List<Book>> {
        return try {
            val response = booksApi.getBooks()
            val books = response.data.map { it.toDomain() }
            BooksResult.Success(books)
        } catch (e: HttpException) {
            mapHttpExceptionToError(e)
        } catch (_: SocketTimeoutException) {
            BooksResult.Error(
                BooksErrorType.TIMEOUT,
                "Request timeout. Please try again."
            )
        } catch (_: IOException) {
            BooksResult.Error(
                BooksErrorType.NETWORK_ERROR,
                "Network error. Please check your connection."
            )
        } catch (e: Exception) {
            BooksResult.Error(
                BooksErrorType.UNKNOWN,
                e.message ?: "An unexpected error occurred"
            )
        }
    }

    private fun mapHttpExceptionToError(exception: HttpException): BooksResult.Error {
        return when (exception.code()) {
            401 -> BooksResult.Error(
                BooksErrorType.UNAUTHORIZED,
                "Session expired. Please login again."
            )
            in 500..599 -> BooksResult.Error(
                BooksErrorType.SERVER_ERROR,
                "Server error. Please try again later."
            )
            else -> BooksResult.Error(
                BooksErrorType.UNKNOWN,
                "An error occurred"
            )
        }
    }
}
