package org.cescfe.book_publishing_app.data.author.repository

import org.cescfe.book_publishing_app.data.author.remote.api.AuthorsApi
import org.cescfe.book_publishing_app.data.author.remote.dto.toDTO
import org.cescfe.book_publishing_app.data.author.remote.dto.toDomain
import org.cescfe.book_publishing_app.data.shared.repository.RepositoryErrorHandler
import org.cescfe.book_publishing_app.domain.author.model.Author
import org.cescfe.book_publishing_app.domain.author.model.AuthorSummary
import org.cescfe.book_publishing_app.domain.author.model.CreateAuthorRequest
import org.cescfe.book_publishing_app.domain.author.model.UpdateAuthorRequest
import org.cescfe.book_publishing_app.domain.author.repository.AuthorsRepository
import org.cescfe.book_publishing_app.domain.shared.DomainResult

class AuthorsRepositoryImpl(private val authorsApi: AuthorsApi) : AuthorsRepository {
    override suspend fun getAuthors(): DomainResult<List<AuthorSummary>> = try {
        val response = authorsApi.getAuthors()
        val authors = response.data.map { it.toDomain() }
        DomainResult.Success(authors)
    } catch (e: Exception) {
        RepositoryErrorHandler.handleException(e)
    }

    override suspend fun getAuthorById(authorId: String): DomainResult<Author> = try {
        val authorDto = authorsApi.getAuthorById(authorId)
        val author = authorDto.toDomain()
        DomainResult.Success(author)
    } catch (e: Exception) {
        RepositoryErrorHandler.handleException(e)
    }

    override suspend fun createAuthor(request: CreateAuthorRequest): DomainResult<Author> = try {
        val authorDto = authorsApi.createAuthor(request.toDTO())
        DomainResult.Success(authorDto.toDomain())
    } catch (e: Exception) {
        RepositoryErrorHandler.handleException(e)
    }

    override suspend fun updateAuthor(authorId: String, request: UpdateAuthorRequest): DomainResult<Author> = try {
        val authorDto = authorsApi.updateAuthor(authorId, request.toDTO())
        DomainResult.Success(authorDto.toDomain())
    } catch (e: Exception) {
        RepositoryErrorHandler.handleException(e)
    }

    override suspend fun deleteAuthorById(authorId: String): DomainResult<Unit> = try {
        authorsApi.deleteAuthorById(authorId)
        DomainResult.Success(Unit)
    } catch (e: Exception) {
        RepositoryErrorHandler.handleException(e)
    }
}
