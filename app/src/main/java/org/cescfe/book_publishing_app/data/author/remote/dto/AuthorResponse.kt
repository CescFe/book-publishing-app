package org.cescfe.book_publishing_app.data.author.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthorResponse(val data: AuthorDTO)
