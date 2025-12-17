package org.cescfe.book_publishing_app.domain.author.model

data class Author(
    val id: String,
    val fullName: String,
    val pseudonym: String?,
    val biography: String?,
    val email: String?,
    val website: String?
)