package org.cescfe.book_publishing_app.ui.shared.permissions

import org.cescfe.book_publishing_app.data.auth.TokenManager
import org.cescfe.book_publishing_app.domain.auth.model.AuthToken
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PermissionCheckerTest {

    @Before
    fun setup() {
        TokenManager.clearToken()
    }

    @After
    fun tearDown() {
        TokenManager.clearToken()
    }

    @Test
    fun `isAdmin should return true for admin user`() {
        val adminToken = AuthToken(
            accessToken = "admin_token",
            tokenType = "Bearer",
            expiresIn = 3600,
            scope = "read write delete",
            userId = "admin123"
        )
        TokenManager.saveAuthToken(adminToken)

        assertTrue(PermissionChecker.isAdmin())
    }

    @Test
    fun `isAdmin should return false for read-only user`() {
        val readOnlyToken = AuthToken(
            accessToken = "readonly_token",
            tokenType = "Bearer",
            expiresIn = 3600,
            scope = "read",
            userId = "user123"
        )
        TokenManager.saveAuthToken(readOnlyToken)

        assertFalse(PermissionChecker.isAdmin())
    }

    @Test
    fun `isAdmin should return false when no token exists`() {
        assertFalse(PermissionChecker.isAdmin())
    }

    @Test
    fun `hasWritePermission should return true for admin user`() {
        val adminToken = AuthToken(
            accessToken = "admin_token",
            tokenType = "Bearer",
            expiresIn = 3600,
            scope = "read write delete",
            userId = "admin123"
        )
        TokenManager.saveAuthToken(adminToken)

        assertTrue(PermissionChecker.hasWritePermission())
    }

    @Test
    fun `hasWritePermission should return false for read-only user`() {
        val readOnlyToken = AuthToken(
            accessToken = "readonly_token",
            tokenType = "Bearer",
            expiresIn = 3600,
            scope = "read",
            userId = "user123"
        )
        TokenManager.saveAuthToken(readOnlyToken)

        assertFalse(PermissionChecker.hasWritePermission())
    }

    @Test
    fun `hasDeletePermission should return true for admin user`() {
        val adminToken = AuthToken(
            accessToken = "admin_token",
            tokenType = "Bearer",
            expiresIn = 3600,
            scope = "read write delete",
            userId = "admin123"
        )
        TokenManager.saveAuthToken(adminToken)

        assertTrue(PermissionChecker.hasDeletePermission())
    }

    @Test
    fun `hasDeletePermission should return false for read-only user`() {
        val readOnlyToken = AuthToken(
            accessToken = "readonly_token",
            tokenType = "Bearer",
            expiresIn = 3600,
            scope = "read",
            userId = "user123"
        )
        TokenManager.saveAuthToken(readOnlyToken)

        assertFalse(PermissionChecker.hasDeletePermission())
    }

    @Test
    fun `hasReadPermission should return true for admin users`() {
        val readOnlyToken = AuthToken(
            accessToken = "readonly_token",
            tokenType = "Bearer",
            expiresIn = 3600,
            scope = "read write delete",
            userId = "user123"
        )
        TokenManager.saveAuthToken(readOnlyToken)

        assertTrue(PermissionChecker.hasReadPermission())
    }

    @Test
    fun `isReadOnly should return true for read-only user`() {
        val readOnlyToken = AuthToken(
            accessToken = "readonly_token",
            tokenType = "Bearer",
            expiresIn = 3600,
            scope = "read",
            userId = "user123"
        )
        TokenManager.saveAuthToken(readOnlyToken)

        assertTrue(PermissionChecker.isReadOnly())
    }

    @Test
    fun `isReadOnly should return false for admin user`() {
        val adminToken = AuthToken(
            accessToken = "admin_token",
            tokenType = "Bearer",
            expiresIn = 3600,
            scope = "read write delete",
            userId = "admin123"
        )
        TokenManager.saveAuthToken(adminToken)

        assertFalse(PermissionChecker.isReadOnly())
    }

    @Test
    fun `hasScope should return true for existing scope`() {
        val token = AuthToken(
            accessToken = "token",
            tokenType = "Bearer",
            expiresIn = 3600,
            scope = "read write",
            userId = "user123"
        )
        TokenManager.saveAuthToken(token)

        assertTrue(PermissionChecker.hasScope("read"))
        assertTrue(PermissionChecker.hasScope("write"))
        assertFalse(PermissionChecker.hasScope("delete"))
    }

    @Test
    fun `hasValidToken should return true when token exists`() {
        val token = AuthToken(
            accessToken = "token",
            tokenType = "Bearer",
            expiresIn = 3600,
            scope = "read",
            userId = "user123"
        )
        TokenManager.saveAuthToken(token)

        assertTrue(PermissionChecker.hasValidToken())
    }

    @Test
    fun `hasValidToken should return false when no token exists`() {
        assertFalse(PermissionChecker.hasValidToken())
    }
}
