package org.cescfe.book_publishing_app.domain.auth.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthTokenTest {

    @Test
    fun `AuthToken should contain all fields from backend response`() {
        val token = AuthToken(
            accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
            tokenType = "Bearer",
            expiresIn = 86400,
            scope = "read write delete",
            userId = "da420b0a-64aa-470d-991e-7fcb7a936229"
        )

        assertEquals("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", token.accessToken)
        assertEquals("Bearer", token.tokenType)
        assertEquals(86400, token.expiresIn)
        assertEquals("read write delete", token.scope)
        assertEquals("da420b0a-64aa-470d-991e-7fcb7a936229", token.userId)
    }

    @Test
    fun `hasScope should return true when scope exists`() {
        val token = AuthToken(
            accessToken = "token",
            expiresIn = 3600,
            scope = "read write delete",
            userId = "user123"
        )

        assertTrue(token.hasScope("read"))
        assertTrue(token.hasScope("write"))
        assertTrue(token.hasScope("delete"))
        assertFalse(token.hasScope("admin"))
    }

    @Test
    fun `isAdmin should return true when token has read write and delete scopes`() {
        val adminToken = AuthToken(
            accessToken = "admin_token",
            expiresIn = 3600,
            scope = "read write delete",
            userId = "admin123"
        )

        val userToken = AuthToken(
            accessToken = "user_token",
            expiresIn = 3600,
            scope = "read",
            userId = "user123"
        )

        assertTrue(adminToken.isAdmin())
        assertFalse(userToken.isAdmin())
    }

    @Test
    fun `isUser should return true when token has only read scope`() {
        val userToken = AuthToken(
            accessToken = "user_token",
            expiresIn = 3600,
            scope = "read",
            userId = "user123"
        )

        val adminToken = AuthToken(
            accessToken = "admin_token",
            expiresIn = 3600,
            scope = "read write delete",
            userId = "admin123"
        )

        assertTrue(userToken.isGuest())
        assertFalse(adminToken.isGuest())
    }
}
