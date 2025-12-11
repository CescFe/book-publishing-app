package org.cescfe.book_publishing_app.data.auth

object TokenManager {
    private var accessToken: String? = null
    private var expiresAt: Long = 0L

    fun saveToken(token: String, expiresInSeconds: Int) {
        accessToken = token
        expiresAt = System.currentTimeMillis() + (expiresInSeconds * 1000L)
    }

    fun getToken(): String? {
        if (isTokenExpired()) {
            clearToken()
            return null
        }
        return accessToken
    }

    fun isTokenExpired(): Boolean {
        return System.currentTimeMillis() >= expiresAt
    }

    fun hasValidToken(): Boolean = getToken() != null

    fun clearToken() {
        accessToken = null
        expiresAt = 0L
    }
}
