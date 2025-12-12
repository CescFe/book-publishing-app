package org.cescfe.book_publishing_app.data.author.repository

import java.io.IOException
import java.net.SocketTimeoutException
import org.cescfe.book_publishing_app.data.author.remote.api.AuthorsApi
import org.cescfe.book_publishing_app.data.author.remote.dto.toDomain
import org.cescfe.book_publishing_app.domain.author.model.AuthorSummary
import org.cescfe.book_publishing_app.domain.author.model.AuthorsResult
import org.cescfe.book_publishing_app.domain.author.repository.AuthorsRepository
import org.cescfe.book_publishing_app.domain.shared.DomainErrorType
import retrofit2.HttpException

class AuthorsRepositoryImpl(private val authorsApi: AuthorsApi) : AuthorsRepository {
    override suspend fun getAuthors(): AuthorsResult<List<AuthorSummary>> = try {
        val response = authorsApi.getAuthors()
        val authors = response.data.map { it.toDomain() }
        AuthorsResult.Success(authors)
    } catch (e: HttpException) {
        mapHttpExceptionToError(e)
    } catch (_: SocketTimeoutException) {
        AuthorsResult.Error(
            DomainErrorType.TIMEOUT,
            "Request timeout. Please try again."
        )
    } catch (_: IOException) {
        AuthorsResult.Error(
            DomainErrorType.NETWORK_ERROR,
            "Network error. Please check your connection."
        )
    } catch (e: Exception) {
        AuthorsResult.Error(
            DomainErrorType.UNKNOWN,
            e.message ?: "An unexpected error occurred"
        )
    }

    private fun mapHttpExceptionToError(ex: HttpException): AuthorsResult.Error = when (ex.code()) {
        401 -> AuthorsResult.Error(
            DomainErrorType.UNAUTHORIZED,
            "Session expired. Please login again."
        )
        in 500..599 -> AuthorsResult.Error(
            DomainErrorType.SERVER_ERROR,
            "Server error. Please try again later."
        )
        else -> AuthorsResult.Error(
            DomainErrorType.UNKNOWN,
            "An error occurred"
        )
    }
}
