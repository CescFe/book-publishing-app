package org.cescfe.book_publishing_app.data.auth

import org.cescfe.book_publishing_app.domain.auth.model.AuthToken
import org.cescfe.book_publishing_app.domain.auth.model.hasScope
import org.cescfe.book_publishing_app.domain.auth.model.isAdmin

object TokenManager {
    private var authToken: AuthToken? = null
    private var expiresAt: Long = 0L

    fun saveAuthToken(token: AuthToken) {
        authToken = token
        expiresAt = System.currentTimeMillis() + (token.expiresIn * 1000L)
    }

    fun getAuthToken(): AuthToken? {
        if (isTokenExpired()) {
            clearToken()
            return null
        }
        return authToken
    }

    fun getToken(): String? = getAuthToken()?.accessToken

    fun isTokenExpired(): Boolean = System.currentTimeMillis() >= expiresAt

    fun hasValidToken(): Boolean = getAuthToken() != null

    fun isAdmin(): Boolean = getAuthToken()?.isAdmin() ?: false

    fun hasScope(requiredScope: String): Boolean = getAuthToken()?.hasScope(requiredScope) ?: false

    fun clearToken() {
        authToken = null
        expiresAt = 0L
    }

    @Deprecated("Use saveAuthToken(AuthToken) instead", ReplaceWith("saveAuthToken(AuthToken)"))
    fun saveToken(token: String, expiresInSeconds: Int) {
        authToken = AuthToken(
            accessToken = token,
            tokenType = "Bearer",
            expiresIn = expiresInSeconds,
            scope = "",
            userId = ""
        )
        expiresAt = System.currentTimeMillis() + (expiresInSeconds * 1000L)
    }
}
