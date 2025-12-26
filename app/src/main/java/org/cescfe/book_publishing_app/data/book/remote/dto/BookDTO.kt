package org.cescfe.book_publishing_app.data.book.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.cescfe.book_publishing_app.domain.book.model.Book
import org.cescfe.book_publishing_app.domain.book.model.enums.Status
import org.cescfe.book_publishing_app.domain.shared.enums.Genre
import org.cescfe.book_publishing_app.domain.shared.enums.Language
import org.cescfe.book_publishing_app.domain.shared.enums.ReadingLevel

@Serializable
data class BookDTO(
    val id: String,
    val title: String,
    @SerialName("base_price")
    val basePrice: Double,
    val author: AuthorRefDTO,
    val collection: CollectionRefDTO,
    @SerialName("reading_level")
    val readingLevel: String? = null,
    @SerialName("primary_language")
    val primaryLanguage: String? = null,
    @SerialName("secondary_languages")
    val secondaryLanguages: List<String>? = null,
    @SerialName("primary_genre")
    val primaryGenre: String? = null,
    @SerialName("secondary_genres")
    val secondaryGenres: List<String>? = null,
    @SerialName("vat_rate")
    val vatRate: Double,
    @SerialName("final_price")
    val finalPrice: Double,
    val isbn: String? = null,
    @SerialName("publication_date")
    val publicationDate: String? = null,
    @SerialName("page_count")
    val pageCount: Int? = null,
    val description: String? = null,
    val status: Status? = null
)

fun BookDTO.toDomain(): Book = Book(
    id = id,
    title = title,
    basePrice = basePrice,
    authorName = author.name,
    collectionName = collection.name,
    readingLevel = readingLevel?.let { ReadingLevel.valueOf(it) },
    primaryLanguage = primaryLanguage?.let { Language.valueOf(it) },
    secondaryLanguages = secondaryLanguages?.map { Language.valueOf(it) } ?: emptyList(),
    primaryGenre = primaryGenre?.let { Genre.valueOf(it) },
    secondaryGenres = secondaryGenres?.map { Genre.valueOf(it) } ?: emptyList(),
    vatRate = vatRate,
    finalPrice = finalPrice,
    isbn = isbn,
    publicationDate = publicationDate,
    pageCount = pageCount,
    description = description,
    status = status ?: Status.DRAFT
)
