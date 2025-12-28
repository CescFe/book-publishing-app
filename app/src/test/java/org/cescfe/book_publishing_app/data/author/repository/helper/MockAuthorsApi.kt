package org.cescfe.book_publishing_app.data.author.repository.helper

import org.cescfe.book_publishing_app.data.author.remote.api.AuthorsApi
import org.cescfe.book_publishing_app.data.author.remote.dto.AuthorDTO
import org.cescfe.book_publishing_app.data.author.remote.dto.AuthorsResponse
import org.cescfe.book_publishing_app.data.author.remote.dto.CreateAuthorRequestDTO
import org.cescfe.book_publishing_app.data.author.remote.dto.UpdateAuthorRequestDTO
import retrofit2.HttpException

class MockAuthorsApi : AuthorsApi {
    var successResponse: AuthorsResponse? = null
    var httpException: HttpException? = null
    var exception: Throwable? = null

    var authorResponse: AuthorDTO? = null
    var createAuthorRequest: CreateAuthorRequestDTO? = null

    var updateAuthorRequest: UpdateAuthorRequestDTO? = null
    var updateAuthorId: String? = null

    var deleteSuccess: Boolean = false
    var deleteAuthorId: String? = null

    override suspend fun getAuthors(): AuthorsResponse = when {
        httpException != null -> throw httpException!!
        exception != null -> throw exception!!
        successResponse != null -> successResponse!!
        else -> throw RuntimeException("Mock not configured")
    }

    override suspend fun getAuthorById(id: String): AuthorDTO = when {
        httpException != null -> throw httpException!!
        exception != null -> throw exception!!
        authorResponse != null -> authorResponse!!
        else -> throw RuntimeException("Mock not configured for getAuthorById")
    }

    override suspend fun createAuthor(request: CreateAuthorRequestDTO): AuthorDTO {
        createAuthorRequest = request
        return when {
            httpException != null -> throw httpException!!
            exception != null -> throw exception!!
            authorResponse != null -> authorResponse!!
            else -> throw RuntimeException("Mock not configured for createAuthor")
        }
    }

    override suspend fun updateAuthor(authorId: String, request: UpdateAuthorRequestDTO): AuthorDTO {
        updateAuthorId = authorId
        updateAuthorRequest = request
        return when {
            httpException != null -> throw httpException!!
            exception != null -> throw exception!!
            authorResponse != null -> authorResponse!!
            else -> throw RuntimeException("Mock not configured for updateAuthor")
        }
    }

    override suspend fun deleteAuthorById(authorId: String) {
        deleteAuthorId = authorId
        when {
            httpException != null -> throw httpException!!
            exception != null -> throw exception!!
            deleteSuccess -> Unit
            else -> throw RuntimeException("Mock not configured for deleteAuthorById")
        }
    }
}
