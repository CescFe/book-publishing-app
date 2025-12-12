package org.cescfe.book_publishing_app.data.author.remote.dto

import org.junit.Assert.assertEquals
import org.junit.Test

class AuthorSummaryDTOTest {

    @Test
    fun `toDomain should map all fields correctly`() {
        val dto = AuthorSummaryDTO(
            id = "author-123",
            fullName = "J.R.R. Tolkien",
            pseudonym = "Tolkien",
            email = "tolkien@example.com"
        )

        val author = dto.toDomain()

        assertEquals(dto.id, author.id)
        assertEquals(dto.fullName, author.fullName)
        assertEquals(dto.pseudonym, author.pseudonym)
        assertEquals(dto.email, author.email)
    }

    @Test
    fun `toDomain should return empty strings when both pseudonym and email are null`() {
        val dto = AuthorSummaryDTO(
            id = "author-123",
            fullName = "Anonymous Author",
            pseudonym = null,
            email = null
        )

        val author = dto.toDomain()

        assertEquals("", author.pseudonym)
        assertEquals("", author.email)
    }
}
