package org.cescfe.book_publishing_app.domain.repository

import org.cescfe.book_publishing_app.domain.model.AuthResult

interface AuthRepository {
    suspend fun login(username: String, password: String): AuthResult
}
