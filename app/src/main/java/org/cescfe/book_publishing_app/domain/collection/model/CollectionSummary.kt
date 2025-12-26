package org.cescfe.book_publishing_app.domain.collection.model

import org.cescfe.book_publishing_app.domain.shared.enums.Genre
import org.cescfe.book_publishing_app.domain.shared.enums.Language
import org.cescfe.book_publishing_app.domain.shared.enums.ReadingLevel

data class CollectionSummary(
    val id: String,
    val name: String,
    val readingLevel: ReadingLevel?,
    val primaryLanguage: Language?,
    val primaryGenre: Genre?
)
