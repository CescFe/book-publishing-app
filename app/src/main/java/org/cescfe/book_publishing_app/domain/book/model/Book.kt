package org.cescfe.book_publishing_app.domain.book.model

data class Book(
    val id: String,
    val title: String,
    val author: String,
    val collection: String,
    val finalPrice: Double,
    val isbn: String?
)
