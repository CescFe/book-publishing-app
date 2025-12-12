package org.cescfe.book_publishing_app.domain.author.repository

import org.cescfe.book_publishing_app.domain.author.model.AuthorSummary
import org.cescfe.book_publishing_app.domain.author.model.AuthorsResult

interface AuthorsRepository {
    suspend fun getAuthors(): AuthorsResult<List<AuthorSummary>>
}
