package org.cescfe.book_publishing_app.ui.collection.helper

import org.cescfe.book_publishing_app.domain.collection.model.CollectionSummary

object TestCollectionFactory {
    fun createCollectionSummary(
        id: String = "default-id",
        name: String = "Default Name",
        readingLevel: String? = null,
        primaryLanguage: String? = null,
        primaryGenre: String? = null
    ) = CollectionSummary(
        id = id,
        name = name,
        readingLevel = readingLevel,
        primaryLanguage = primaryLanguage,
        primaryGenre = primaryGenre
    )
}
