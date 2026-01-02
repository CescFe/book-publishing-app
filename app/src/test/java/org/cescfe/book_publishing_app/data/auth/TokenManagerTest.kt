package org.cescfe.book_publishing_app.data.auth

import org.cescfe.book_publishing_app.domain.auth.model.AuthToken
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TokenManagerTest {

    @Before
    fun setup() {
        TokenManager.clearToken()
    }

    @After
    fun tearDown() {
        TokenManager.clearToken()
    }

    @Test
    fun `saveAuthToken should store token and make it retrievable`() {
        val authToken = AuthToken(
            accessToken = "test_token",
            tokenType = "Bearer",
            expiresIn = 3600,
            scope = "read write delete",
            userId = "user123"
        )
        TokenManager.saveAuthToken(authToken)

        val retrieved = TokenManager.getAuthToken()
        assertTrue(retrieved != null)
        assertEquals("test_token", retrieved?.accessToken)
        assertEquals("read write delete", retrieved?.scope)
        assertEquals("user123", retrieved?.userId)
    }

    @Test
    fun `getToken should return access token string`() {
        val authToken = AuthToken(
            accessToken = "test_token",
            tokenType = "Bearer",
            expiresIn = 3600,
            scope = "read write delete",
            userId = "user123"
        )
        TokenManager.saveAuthToken(authToken)

        assertEquals("test_token", TokenManager.getToken())
    }

    @Test
    fun `getToken should return null when no token saved`() {
        assertNull(TokenManager.getToken())
    }

    @Test
    fun `getAuthToken should return null when no token saved`() {
        assertNull(TokenManager.getAuthToken())
    }

    @Test
    fun `hasValidToken should return true when valid token exists`() {
        val authToken = AuthToken(
            accessToken = "test_token",
            tokenType = "Bearer",
            expiresIn = 3600,
            scope = "read",
            userId = "user123"
        )
        TokenManager.saveAuthToken(authToken)

        assertTrue(TokenManager.hasValidToken())
    }

    @Test
    fun `hasValidToken should return false when no token exists`() {
        assertFalse(TokenManager.hasValidToken())
    }

    @Test
    fun `clearToken should remove stored token`() {
        val authToken = AuthToken(
            accessToken = "test_token",
            tokenType = "Bearer",
            expiresIn = 3600,
            scope = "read",
            userId = "user123"
        )
        TokenManager.saveAuthToken(authToken)
        TokenManager.clearToken()

        assertNull(TokenManager.getToken())
        assertNull(TokenManager.getAuthToken())
        assertFalse(TokenManager.hasValidToken())
    }

    @Test
    fun `getToken should return null when token is expired`() {
        val authToken = AuthToken(
            accessToken = "test_token",
            tokenType = "Bearer",
            expiresIn = 0,
            scope = "read",
            userId = "user123"
        )
        TokenManager.saveAuthToken(authToken)

        Thread.sleep(10)

        assertNull(TokenManager.getToken())
    }

    @Test
    fun `getAuthToken should return null when token is expired`() {
        val authToken = AuthToken(
            accessToken = "test_token",
            tokenType = "Bearer",
            expiresIn = 0,
            scope = "read",
            userId = "user123"
        )
        TokenManager.saveAuthToken(authToken)

        Thread.sleep(10)

        assertNull(TokenManager.getAuthToken())
    }

    @Test
    fun `isTokenExpired should return true when token is expired`() {
        val authToken = AuthToken(
            accessToken = "test_token",
            tokenType = "Bearer",
            expiresIn = 0,
            scope = "read",
            userId = "user123"
        )
        TokenManager.saveAuthToken(authToken)
        Thread.sleep(10)

        assertTrue(TokenManager.isTokenExpired())
    }

    @Test
    fun `isTokenExpired should return false when token is valid`() {
        val authToken = AuthToken(
            accessToken = "test_token",
            tokenType = "Bearer",
            expiresIn = 3600,
            scope = "read",
            userId = "user123"
        )
        TokenManager.saveAuthToken(authToken)

        assertFalse(TokenManager.isTokenExpired())
    }

    @Test
    fun `getToken should clear expired token automatically`() {
        val authToken = AuthToken(
            accessToken = "test_token",
            tokenType = "Bearer",
            expiresIn = 0,
            scope = "read",
            userId = "user123"
        )
        TokenManager.saveAuthToken(authToken)
        Thread.sleep(10)

        TokenManager.getToken()

        assertFalse(TokenManager.hasValidToken())
    }

    @Test
    fun `isAdmin should return true for admin user`() {
        val authToken = AuthToken(
            accessToken = "test_token",
            tokenType = "Bearer",
            expiresIn = 3600,
            scope = "read write delete",
            userId = "admin123"
        )
        TokenManager.saveAuthToken(authToken)

        assertTrue(TokenManager.isAdmin())
    }

    @Test
    fun `isAdmin should return false for read-only user`() {
        val authToken = AuthToken(
            accessToken = "test_token",
            tokenType = "Bearer",
            expiresIn = 3600,
            scope = "read",
            userId = "user123"
        )
        TokenManager.saveAuthToken(authToken)

        assertFalse(TokenManager.isAdmin())
    }

    @Test
    fun `isAdmin should return false when no token exists`() {
        assertFalse(TokenManager.isAdmin())
    }

    @Test
    fun `hasScope should return true when scope exists`() {
        val authToken = AuthToken(
            accessToken = "test_token",
            tokenType = "Bearer",
            expiresIn = 3600,
            scope = "read write",
            userId = "user123"
        )
        TokenManager.saveAuthToken(authToken)

        assertTrue(TokenManager.hasScope("read"))
        assertTrue(TokenManager.hasScope("write"))
    }

    @Test
    fun `hasScope should return false when scope does not exist`() {
        val authToken = AuthToken(
            accessToken = "test_token",
            tokenType = "Bearer",
            expiresIn = 3600,
            scope = "read",
            userId = "user123"
        )
        TokenManager.saveAuthToken(authToken)

        assertFalse(TokenManager.hasScope("write"))
        assertFalse(TokenManager.hasScope("delete"))
    }

    @Test
    fun `hasScope should return false when no token exists`() {
        assertFalse(TokenManager.hasScope("read"))
    }

    @Test
    fun `hasScope should handle multiple scopes correctly`() {
        val authToken = AuthToken(
            accessToken = "test_token",
            tokenType = "Bearer",
            expiresIn = 3600,
            scope = "read write delete",
            userId = "admin123"
        )
        TokenManager.saveAuthToken(authToken)

        assertTrue(TokenManager.hasScope("read"))
        assertTrue(TokenManager.hasScope("write"))
        assertTrue(TokenManager.hasScope("delete"))
    }
}
