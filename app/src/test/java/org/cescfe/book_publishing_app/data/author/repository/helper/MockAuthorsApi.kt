package org.cescfe.book_publishing_app.data.author.repository.helper

import org.cescfe.book_publishing_app.data.author.remote.api.AuthorsApi
import org.cescfe.book_publishing_app.data.author.remote.dto.AuthorDTO
import org.cescfe.book_publishing_app.data.author.remote.dto.AuthorsResponse
import org.cescfe.book_publishing_app.data.author.remote.dto.CreateAuthorRequestDTO
import org.cescfe.book_publishing_app.data.author.remote.dto.UpdateAuthorRequestDTO
import org.cescfe.book_publishing_app.data.shared.repository.helper.MockResult
import org.cescfe.book_publishing_app.data.shared.repository.helper.resolve

class MockAuthorsApi : AuthorsApi {
    var getAuthorsResult: MockResult<AuthorsResponse>? = null
    var getAuthorResult: MockResult<AuthorDTO>? = null
    var createAuthorResult: MockResult<AuthorDTO>? = null
    var updateAuthorResult: MockResult<AuthorDTO>? = null
    var deleteAuthorResult: MockResult<Unit>? = null

    var createAuthorRequest: CreateAuthorRequestDTO? = null
    var updateAuthorRequest: UpdateAuthorRequestDTO? = null
    var updateAuthorId: String? = null
    var deleteAuthorId: String? = null

    override suspend fun getAuthors(): AuthorsResponse = getAuthorsResult.resolve()

    override suspend fun getAuthorById(id: String): AuthorDTO = getAuthorResult.resolve()

    override suspend fun createAuthor(request: CreateAuthorRequestDTO): AuthorDTO {
        createAuthorRequest = request
        return createAuthorResult.resolve()
    }

    override suspend fun updateAuthor(authorId: String, request: UpdateAuthorRequestDTO): AuthorDTO {
        updateAuthorId = authorId
        updateAuthorRequest = request
        return updateAuthorResult.resolve()
    }

    override suspend fun deleteAuthorById(authorId: String) {
        deleteAuthorId = authorId
        deleteAuthorResult.resolve()
    }
}
