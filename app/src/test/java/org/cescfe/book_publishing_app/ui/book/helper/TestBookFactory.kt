package org.cescfe.book_publishing_app.ui.book.helper

import org.cescfe.book_publishing_app.domain.book.model.Book
import org.cescfe.book_publishing_app.domain.book.model.BookSummary
import org.cescfe.book_publishing_app.domain.book.model.enums.Status
import org.cescfe.book_publishing_app.domain.shared.enums.Genre
import org.cescfe.book_publishing_app.domain.shared.enums.Language
import org.cescfe.book_publishing_app.domain.shared.enums.ReadingLevel

object TestBookFactory {
    fun createBook(
        id: String = "default-id",
        title: String = "Default Title",
        basePrice: Double = 10.0,
        authorName: String = "Default Author",
        collectionName: String = "Default Collection",
        readingLevel: ReadingLevel? = null,
        primaryLanguage: Language? = null,
        secondaryLanguages: List<Language?> = emptyList(),
        primaryGenre: Genre? = null,
        secondaryGenres: List<Genre?> = emptyList(),
        vatRate: Double = 0.04,
        finalPrice: Double = 10.40,
        isbn: String? = null,
        publicationDate: String? = null,
        pageCount: Int? = null,
        description: String? = null,
        status: Status = Status.DRAFT
    ) = Book(
        id = id,
        title = title,
        basePrice = basePrice,
        authorName = authorName,
        collectionName = collectionName,
        readingLevel = readingLevel,
        primaryLanguage = primaryLanguage,
        secondaryLanguages = secondaryLanguages,
        primaryGenre = primaryGenre,
        secondaryGenres = secondaryGenres,
        vatRate = vatRate,
        finalPrice = finalPrice,
        isbn = isbn,
        publicationDate = publicationDate,
        pageCount = pageCount,
        description = description,
        status = status
    )

    fun createBookSummary(
        id: String = "default-id",
        title: String = "Default Title",
        author: String = "Default Author",
        collection: String = "Default Collection",
        finalPrice: Double = 10.0,
        isbn: String = "978-0-000000-00-0"
    ) = BookSummary(
        id = id,
        title = title,
        author = author,
        collection = collection,
        finalPrice = finalPrice,
        isbn = isbn
    )
}
