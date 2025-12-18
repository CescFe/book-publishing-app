package org.cescfe.book_publishing_app.data.author.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.cescfe.book_publishing_app.domain.author.model.CreateAuthorRequest

@Serializable
data class CreateAuthorRequestDTO(
    @SerialName("full_name")
    val fullName: String,
    val pseudonym: String? = null,
    val biography: String? = null,
    val email: String? = null,
    val website: String? = null
)

fun CreateAuthorRequest.toDTO(): CreateAuthorRequestDTO = CreateAuthorRequestDTO(
    fullName = fullName,
    pseudonym = pseudonym,
    biography = biography,
    email = email,
    website = website
)
