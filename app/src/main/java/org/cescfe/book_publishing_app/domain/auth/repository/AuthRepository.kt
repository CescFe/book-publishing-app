package org.cescfe.book_publishing_app.domain.auth.repository

import org.cescfe.book_publishing_app.domain.auth.model.AuthResult

interface AuthRepository {
    suspend fun login(username: String, password: String): AuthResult
}
