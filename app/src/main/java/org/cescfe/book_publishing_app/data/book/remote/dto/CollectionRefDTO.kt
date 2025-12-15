package org.cescfe.book_publishing_app.data.book.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CollectionRefDTO(val id: String, val name: String)
