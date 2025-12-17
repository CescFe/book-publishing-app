package org.cescfe.book_publishing_app.data.author.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.cescfe.book_publishing_app.domain.author.model.Author

@Serializable
data class AuthorDTO(
    val id: String,
    @SerialName("full_name")
    val fullName: String,
    val pseudonym: String? = null,
    val biography: String? = null,
    val email: String? = null,
    val website: String? = null
)

fun AuthorDTO.toDomain(): Author = Author(
    id = id,
    fullName = fullName,
    pseudonym = pseudonym,
    biography = biography,
    email = email,
    website = website
)
