package org.cescfe.book_publishing_app.data.book.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.cescfe.book_publishing_app.domain.book.model.CreateBookRequest

@Serializable
data class CreateBookRequestDTO(
    val title: String,
    @SerialName("author_id")
    val authorId: String,
    @SerialName("collection_id")
    val collectionId: String,
    @SerialName("base_price")
    val basePrice: Double,
    @SerialName("reading_level")
    val readingLevel: String? = null,
    @SerialName("primary_language")
    val primaryLanguage: String? = null,
    @SerialName("secondary_languages")
    val secondaryLanguages: List<String> = emptyList(),
    @SerialName("primary_genre")
    val primaryGenre: String? = null,
    @SerialName("secondary_genres")
    val secondaryGenres: List<String> = emptyList(),
    @SerialName("vat_rate")
    val vatRate: Double? = null,
    val isbn: String? = null,
    @SerialName("publication_date")
    val publicationDate: String? = null,
    @SerialName("page_count")
    val pageCount: Int? = null,
    val description: String? = null,
    val status: String? = null
)

fun CreateBookRequest.toDTO(): CreateBookRequestDTO = CreateBookRequestDTO(
    title = title,
    authorId = authorId,
    collectionId = collectionId,
    basePrice = basePrice,
    readingLevel = readingLevel?.name,
    primaryLanguage = primaryLanguage?.name,
    secondaryLanguages = secondaryLanguages.map { it.name },
    primaryGenre = primaryGenre?.name,
    secondaryGenres = secondaryGenres.map { it.name },
    vatRate = vatRate,
    isbn = isbn,
    publicationDate = publicationDate,
    pageCount = pageCount,
    description = description,
    status = status?.name
)
