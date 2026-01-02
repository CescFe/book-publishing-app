package org.cescfe.book_publishing_app.ui.shared.permissions

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class UserPermissions(
    val isAdmin: Boolean = false,
    val hasWritePermission: Boolean = false,
    val hasDeletePermission: Boolean = false,
    val hasReadPermission: Boolean = false,
    val isReadOnly: Boolean = false
) {
    companion object {
        fun current(): UserPermissions {
            val isAdmin = PermissionChecker.isAdmin()
            val hasWrite = PermissionChecker.hasWritePermission()
            val hasDelete = PermissionChecker.hasDeletePermission()
            val hasRead = PermissionChecker.hasReadPermission()

            return UserPermissions(
                isAdmin = isAdmin,
                hasWritePermission = hasWrite,
                hasDeletePermission = hasDelete,
                hasReadPermission = hasRead,
                isReadOnly = hasRead && !hasWrite
            )
        }
    }
}

class UserPermissionsStateHolder {
    private val _permissions = MutableStateFlow(UserPermissions.current())
    val permissions: StateFlow<UserPermissions> = _permissions.asStateFlow()

    fun refresh() {
        _permissions.value = UserPermissions.current()
    }
}
