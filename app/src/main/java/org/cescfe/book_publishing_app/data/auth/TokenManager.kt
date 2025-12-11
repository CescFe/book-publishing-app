package org.cescfe.book_publishing_app.data.auth

object TokenManager {
    private var accessToken: String? = null

    fun saveToken(token: String) {
        accessToken = token
    }

    fun getToken(): String? = accessToken

    fun clearToken() {
        accessToken = null
    }

    fun hasToken(): Boolean = accessToken != null
}
