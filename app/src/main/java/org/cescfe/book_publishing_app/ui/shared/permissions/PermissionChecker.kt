package org.cescfe.book_publishing_app.ui.shared.permissions

import org.cescfe.book_publishing_app.data.auth.TokenManager

object PermissionChecker {

    fun isAdmin(): Boolean = TokenManager.isAdmin()

    fun hasWritePermission(): Boolean = TokenManager.hasScope("write")

    fun hasDeletePermission(): Boolean = TokenManager.hasScope("delete")

    fun hasReadPermission(): Boolean = TokenManager.hasScope("read")

    fun hasScope(scope: String): Boolean = TokenManager.hasScope(scope)

    fun isReadOnly(): Boolean = hasReadPermission() && !hasWritePermission()

    fun hasValidToken(): Boolean = TokenManager.hasValidToken()
}
