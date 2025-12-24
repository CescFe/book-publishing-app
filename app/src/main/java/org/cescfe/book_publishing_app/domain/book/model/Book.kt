package org.cescfe.book_publishing_app.domain.book.model

import org.cescfe.book_publishing_app.domain.book.model.enums.Status
import org.cescfe.book_publishing_app.domain.shared.enums.Genre
import org.cescfe.book_publishing_app.domain.shared.enums.Language
import org.cescfe.book_publishing_app.domain.shared.enums.ReadingLevel

data class Book(
    val id: String,
    val title: String,
    val basePrice: Double,
    val authorName: String,
    val collectionName: String,
    val readingLevel: ReadingLevel?,
    val primaryLanguage: Language?,
    val secondaryLanguages: List<Language?>,
    val primaryGenre: Genre?,
    val secondaryGenres: List<Genre?>,
    val vatRate: Double,
    val finalPrice: Double,
    val isbn: String?,
    val publicationDate: String?,
    val pageCount: Int?,
    val description: String?,
    val status: Status
)
