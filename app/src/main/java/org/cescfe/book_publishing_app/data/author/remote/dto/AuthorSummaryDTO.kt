package org.cescfe.book_publishing_app.data.author.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.cescfe.book_publishing_app.domain.author.model.AuthorSummary

@Serializable
data class AuthorSummaryDTO(
    val id: String,
    @SerialName("full_name")
    val fullName: String,
    val pseudonym: String? = null,
    val email: String? = null
)

fun AuthorSummaryDTO.toDomain(): AuthorSummary = AuthorSummary(
    id = id,
    fullName = fullName,
    pseudonym = pseudonym ?: "",
    email = email ?: ""
)
