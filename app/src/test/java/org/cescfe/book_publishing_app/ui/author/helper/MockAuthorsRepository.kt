package org.cescfe.book_publishing_app.ui.author.helper

import org.cescfe.book_publishing_app.domain.author.model.Author
import org.cescfe.book_publishing_app.domain.author.model.AuthorSummary
import org.cescfe.book_publishing_app.domain.author.repository.AuthorsRepository
import org.cescfe.book_publishing_app.domain.shared.DomainResult

class MockAuthorsRepository : AuthorsRepository {
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

    override suspend fun getAuthors(): DomainResult<List<AuthorSummary>> = authorsResult

    override suspend fun getAuthorById(authorId: String): DomainResult<Author> = authorResult
}
