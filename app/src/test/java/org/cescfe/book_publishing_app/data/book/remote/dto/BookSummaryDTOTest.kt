package org.cescfe.book_publishing_app.data.book.remote.dto

import org.junit.Assert.assertEquals
import org.junit.Test

class BookSummaryDTOTest {

    @Test
    fun `toDomain should map all fields correctly`() {
        val dto = BookSummaryDTO(
            id = "book-123",
            title = "The Lord of the Rings",
            authorId = "author-456",
            collectionId = "collection-789",
            basePrice = 29.99,
            finalPrice = 24.99,
            isbn = "978-0-544-00341-5",
            status = "PUBLISHED"
        )

        val book = dto.toDomain()

        assertEquals("book-123", book.id)
        assertEquals("The Lord of the Rings", book.title)
        assertEquals("author-456", book.author)
        assertEquals("collection-789", book.collection)
        assertEquals(24.99, book.finalPrice, 0.001)
        assertEquals("978-0-544-00341-5", book.isbn)
    }

    @Test
    fun `toDomain should use basePrice when finalPrice is null`() {
        val dto = BookSummaryDTO(
            id = "book-123",
            title = "Test Book",
            authorId = "author-456",
            collectionId = "collection-789",
            basePrice = 29.99,
            finalPrice = null,
            isbn = null,
            status = null
        )

        val book = dto.toDomain()

        assertEquals(29.99, book.finalPrice, 0.001)
    }

    @Test
    fun `toDomain should return empty string when isbn is null`() {
        val dto = BookSummaryDTO(
            id = "book-123",
            title = "Test Book",
            authorId = "author-456",
            collectionId = "collection-789",
            basePrice = 10.0,
            finalPrice = null,
            isbn = null,
            status = null
        )

        val book = dto.toDomain()

        assertEquals("", book.isbn)
    }
}
