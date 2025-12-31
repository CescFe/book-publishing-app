package org.cescfe.book_publishing_app.domain.book.model

import org.cescfe.book_publishing_app.domain.book.model.enums.Status
import org.cescfe.book_publishing_app.domain.shared.enums.Genre
import org.cescfe.book_publishing_app.domain.shared.enums.Language
import org.cescfe.book_publishing_app.domain.shared.enums.ReadingLevel

data class UpdateBookRequest(
    val title: String,
    val authorId: String,
    val collectionId: String,
    val basePrice: Double,
    val readingLevel: ReadingLevel? = null,
    val primaryLanguage: Language? = null,
    val secondaryLanguages: List<Language> = emptyList(),
    val primaryGenre: Genre? = null,
    val secondaryGenres: List<Genre> = emptyList(),
    val vatRate: Double? = null,
    val isbn: String? = null,
    val publicationDate: String? = null,
    val pageCount: Int? = null,
    val description: String? = null,
    val status: Status? = null
)
