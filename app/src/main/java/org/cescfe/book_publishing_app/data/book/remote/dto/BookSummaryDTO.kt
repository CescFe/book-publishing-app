package org.cescfe.book_publishing_app.data.book.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.cescfe.book_publishing_app.domain.book.model.BookSummary

@Serializable
data class BookSummaryDTO(
    val id: String,
    val title: String,
    @SerialName("author_id")
    val authorId: String,
    @SerialName("collection_id")
    val collectionId: String,
    @SerialName("base_price")
    val basePrice: Double,
    @SerialName("final_price")
    val finalPrice: Double? = null,
    val isbn: String? = null,
    val status: String? = null
)

fun BookSummaryDTO.toDomain(): BookSummary = BookSummary(
    id = id,
    title = title,
    author = authorId,
    collection = collectionId,
    finalPrice = finalPrice ?: basePrice,
    isbn = isbn ?: ""
)
