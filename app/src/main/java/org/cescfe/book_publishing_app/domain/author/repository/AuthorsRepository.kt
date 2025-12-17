package org.cescfe.book_publishing_app.domain.author.repository

import org.cescfe.book_publishing_app.domain.author.model.Author
import org.cescfe.book_publishing_app.domain.author.model.AuthorSummary
import org.cescfe.book_publishing_app.domain.shared.DomainResult

interface AuthorsRepository {
    suspend fun getAuthors(): DomainResult<List<AuthorSummary>>
    suspend fun getAuthorById(authorId: String): DomainResult<Author>
}
