package org.cescfe.book_publishing_app.data.collection.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.cescfe.book_publishing_app.domain.collection.model.CollectionSummary

@Serializable
data class CollectionSummaryDTO(
    val id: String,
    val name: String,
    @SerialName("reading_level")
    val readingLevel: String? = null,
    @SerialName("primary_language")
    val primaryLanguage: String? = null,
    @SerialName("primary_genre")
    val primaryGenre: String? = null
)

fun CollectionSummaryDTO.toDomain(): CollectionSummary = CollectionSummary(
    id = id,
    name = name,
    readingLevel = readingLevel ?: "",
    primaryLanguage = primaryLanguage ?: "",
    primaryGenre = primaryGenre ?: ""
)
