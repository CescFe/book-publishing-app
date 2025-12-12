package org.cescfe.book_publishing_app.domain.collection.model

data class CollectionSummary(
    val id: String,
    val name: String,
    val readingLevel: String?,
    val primaryLanguage: String?,
    val primaryGenre: String?
)
