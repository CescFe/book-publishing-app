package org.cescfe.book_publishing_app.ui.shared.permissions

import org.cescfe.book_publishing_app.data.auth.TokenManager
import org.cescfe.book_publishing_app.domain.auth.model.AuthToken
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UserPermissionsTest {

    @Before
    fun setup() {
        TokenManager.clearToken()
    }

    @After
    fun tearDown() {
        TokenManager.clearToken()
    }

    @Test
    fun `current should return admin permissions for admin user`() {
        val adminToken = AuthToken(
            accessToken = "admin_token",
            tokenType = "Bearer",
            expiresIn = 3600,
            scope = "read write delete",
            userId = "admin123"
        )
        TokenManager.saveAuthToken(adminToken)

        val permissions = UserPermissions.current()

        assertTrue(permissions.isAdmin)
        assertTrue(permissions.hasWritePermission)
        assertTrue(permissions.hasDeletePermission)
        assertTrue(permissions.hasReadPermission)
        assertFalse(permissions.isReadOnly)
    }

    @Test
    fun `current should return read-only permissions for read-only user`() {
        val readOnlyToken = AuthToken(
            accessToken = "readonly_token",
            tokenType = "Bearer",
            expiresIn = 3600,
            scope = "read",
            userId = "user123"
        )
        TokenManager.saveAuthToken(readOnlyToken)

        val permissions = UserPermissions.current()

        assertFalse(permissions.isAdmin)
        assertFalse(permissions.hasWritePermission)
        assertFalse(permissions.hasDeletePermission)
        assertTrue(permissions.hasReadPermission)
        assertTrue(permissions.isReadOnly)
    }

    @Test
    fun `current should return all false when no token exists`() {
        val permissions = UserPermissions.current()

        assertFalse(permissions.isAdmin)
        assertFalse(permissions.hasWritePermission)
        assertFalse(permissions.hasDeletePermission)
        assertFalse(permissions.hasReadPermission)
        assertFalse(permissions.isReadOnly)
    }
}
