package org.cescfe.book_publishing_app.domain.author.model

data class UpdateAuthorRequest(
    val fullName: String,
    val pseudonym: String? = null,
    val biography: String? = null,
    val email: String? = null,
    val website: String? = null
)