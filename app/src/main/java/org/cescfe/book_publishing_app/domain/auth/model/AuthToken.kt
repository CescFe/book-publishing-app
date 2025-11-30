package org.cescfe.book_publishing_app.domain.auth.model

data class AuthToken(
    val accessToken: String,
    val tokenType: String = "Bearer",
    val expiresIn: Int,
    val scope: String,
    val userId: String
)

fun AuthToken.hasScope(requiredScope: String): Boolean = scope.split(" ").contains(requiredScope)

fun AuthToken.isAdmin(): Boolean = hasScope("read") && hasScope("write") && hasScope("delete")

fun AuthToken.isGuest(): Boolean = hasScope("read") && !hasScope("write")
