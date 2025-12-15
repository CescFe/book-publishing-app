package org.cescfe.book_publishing_app.data.book.remote.dto

import org.junit.Assert.assertEquals
import org.junit.Test

class BookSummaryDTOTest {

    @Test
    fun `toDomain should map all fields correctly`() {
        val dto = BookSummaryDTO(
            id = "book-123",
            title = "The Lord of the Rings",
            author = AuthorRefDTO(
                id = "author-456",
                name = "J.R.R. Tolkien"
            ),
            collection = CollectionRefDTO(
                id = "collection-789",
                name = "Fantasy Classics"
            ),
            basePrice = 29.99,
            finalPrice = 24.99,
            isbn = "978-0-544-00341-5",
            status = "PUBLISHED"
        )

        val book = dto.toDomain()

        assertEquals(dto.id, book.id)
        assertEquals(dto.title, book.title)
        assertEquals(dto.author.name, book.author)
        assertEquals(dto.collection.name, book.collection)
        assertEquals(dto.finalPrice!!, book.finalPrice, 0.001)
        assertEquals(dto.isbn, book.isbn)
    }

    @Test
    fun `toDomain should use basePrice when finalPrice is null`() {
        val dto = BookSummaryDTO(
            id = "book-123",
            title = "Test Book",
            author = AuthorRefDTO(
                id = "author-456",
                name = "J.R.R. Tolkien"
            ),
            collection = CollectionRefDTO(
                id = "collection-789",
                name = "Fantasy Classics"
            ),
            basePrice = 29.99,
            finalPrice = null,
            isbn = null,
            status = null
        )

        val book = dto.toDomain()

        assertEquals(dto.basePrice, book.finalPrice, 0.001)
    }

    @Test
    fun `toDomain should return empty string when isbn is null`() {
        val dto = BookSummaryDTO(
            id = "book-123",
            title = "Test Book",
            author = AuthorRefDTO(
                id = "author-456",
                name = "J.R.R. Tolkien"
            ),
            collection = CollectionRefDTO(
                id = "collection-789",
                name = "Fantasy Classics"
            ),
            basePrice = 10.0,
            finalPrice = null,
            isbn = null,
            status = null
        )

        val book = dto.toDomain()

        assertEquals("", book.isbn)
    }
}
