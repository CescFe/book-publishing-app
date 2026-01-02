package org.cescfe.book_publishing_app.ui.shared.permissions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun rememberIsAdmin(): Boolean = remember { PermissionChecker.isAdmin() }

@Composable
fun rememberHasWritePermission(): Boolean = remember { PermissionChecker.hasWritePermission() }

@Composable
fun rememberHasDeletePermission(): Boolean = remember { PermissionChecker.hasDeletePermission() }

@Composable
fun rememberIsReadOnly(): Boolean = remember { PermissionChecker.isReadOnly() }

@Composable
fun rememberHasScope(scope: String): Boolean = remember(scope) {
    PermissionChecker.hasScope(scope)
}
