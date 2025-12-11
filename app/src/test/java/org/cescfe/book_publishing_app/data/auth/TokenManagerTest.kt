package org.cescfe.book_publishing_app.data.auth

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
    fun `saveToken should store token and make it retrievable`() {
        TokenManager.saveToken("test_token", 3600)

        assertEquals("test_token", TokenManager.getToken())
    }

    @Test
    fun `getToken should return null when no token saved`() {
        assertNull(TokenManager.getToken())
    }

    @Test
    fun `hasValidToken should return true when valid token exists`() {
        TokenManager.saveToken("test_token", 3600)

        assertTrue(TokenManager.hasValidToken())
    }

    @Test
    fun `hasValidToken should return false when no token exists`() {
        assertFalse(TokenManager.hasValidToken())
    }

    @Test
    fun `clearToken should remove stored token`() {
        TokenManager.saveToken("test_token", 3600)
        TokenManager.clearToken()

        assertNull(TokenManager.getToken())
        assertFalse(TokenManager.hasValidToken())
    }

    @Test
    fun `getToken should return null when token is expired`() {
        TokenManager.saveToken("test_token", 0)

        Thread.sleep(10)

        assertNull(TokenManager.getToken())
    }

    @Test
    fun `isTokenExpired should return true when token is expired`() {
        TokenManager.saveToken("test_token", 0)
        Thread.sleep(10)

        assertTrue(TokenManager.isTokenExpired())
    }

    @Test
    fun `isTokenExpired should return false when token is valid`() {
        TokenManager.saveToken("test_token", 3600)

        assertFalse(TokenManager.isTokenExpired())
    }

    @Test
    fun `getToken should clear expired token automatically`() {
        TokenManager.saveToken("test_token", 0)
        Thread.sleep(10)

        TokenManager.getToken()

        assertFalse(TokenManager.hasValidToken())
    }
}
