package org.cescfe.book_publishing_app.data.author.remote.dto

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AuthorDTOTest {

    @Test
    fun `toDomain should map all fields correctly`() {
        val dto = AuthorDTO(
            id = "author-123",
            fullName = "J.R.R. Tolkien",
            pseudonym = "Tolkien",
            biography = "English writer and philologist",
            email = "tolkien@example.com",
            website = "https://www.tolkienestate.com"
        )

        val author = dto.toDomain()

        assertEquals(dto.id, author.id)
        assertEquals(dto.fullName, author.fullName)
        assertEquals(dto.pseudonym, author.pseudonym)
        assertEquals(dto.biography, author.biography)
        assertEquals(dto.email, author.email)
        assertEquals(dto.website, author.website)
    }

    @Test
    fun `toDomain should handle null optional fields correctly`() {
        val dto = AuthorDTO(
            id = "author-456",
            fullName = "George Orwell",
            pseudonym = null,
            biography = null,
            email = null,
            website = null
        )

        val author = dto.toDomain()

        assertEquals(dto.id, author.id)
        assertEquals(dto.fullName, author.fullName)
        assertNull(author.pseudonym)
        assertNull(author.biography)
        assertNull(author.email)
        assertNull(author.website)
    }
}
