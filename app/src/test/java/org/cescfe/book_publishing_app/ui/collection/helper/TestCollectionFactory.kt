package org.cescfe.book_publishing_app.ui.collection.helper

import org.cescfe.book_publishing_app.domain.collection.model.CollectionSummary
import org.cescfe.book_publishing_app.domain.shared.enums.Genre
import org.cescfe.book_publishing_app.domain.shared.enums.Language
import org.cescfe.book_publishing_app.domain.shared.enums.ReadingLevel

object TestCollectionFactory {
    fun createCollectionSummary(
        id: String = "default-id",
        name: String = "Default Name",
        readingLevel: ReadingLevel? = null,
        primaryLanguage: Language? = null,
        primaryGenre: Genre? = null
    ) = CollectionSummary(
        id = id,
        name = name,
        readingLevel = readingLevel,
        primaryLanguage = primaryLanguage,
        primaryGenre = primaryGenre
    )
}
