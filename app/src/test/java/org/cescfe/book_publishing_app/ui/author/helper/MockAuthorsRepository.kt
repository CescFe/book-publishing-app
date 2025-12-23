package org.cescfe.book_publishing_app.ui.author.helper

import org.cescfe.book_publishing_app.domain.author.model.Author
import org.cescfe.book_publishing_app.domain.author.model.AuthorSummary
import org.cescfe.book_publishing_app.domain.author.model.CreateAuthorRequest
import org.cescfe.book_publishing_app.domain.author.model.UpdateAuthorRequest
import org.cescfe.book_publishing_app.domain.author.repository.AuthorsRepository
import org.cescfe.book_publishing_app.domain.shared.DomainErrorType
import org.cescfe.book_publishing_app.domain.shared.DomainResult

class MockAuthorsRepository : AuthorsRepository {
    var createAuthorResult: DomainResult<Author> = DomainResult.Error(DomainErrorType.UNKNOWN)
    var authorsResult: DomainResult<List<AuthorSummary>> = DomainResult.Success(emptyList())
    var authorResult: DomainResult<Author> = DomainResult.Success(
        Author(
            id = "default",
            fullName = "Default",
            pseudonym = null,
            biography = null,
            email = null,
            website = null
        )
    )
    var updateAuthorResult: DomainResult<Author> = DomainResult.Error(DomainErrorType.UNKNOWN)
    var deleteResult: DomainResult<Unit> = DomainResult.Success(Unit)

    override suspend fun createAuthor(request: CreateAuthorRequest): DomainResult<Author> = createAuthorResult

    override suspend fun updateAuthor(authorId: String, request: UpdateAuthorRequest): DomainResult<Author> =
        updateAuthorResult

    override suspend fun getAuthors(): DomainResult<List<AuthorSummary>> = authorsResult

    override suspend fun getAuthorById(authorId: String): DomainResult<Author> = authorResult

    override suspend fun deleteAuthorById(authorId: String): DomainResult<Unit> = deleteResult
}
