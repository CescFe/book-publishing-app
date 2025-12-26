package org.cescfe.book_publishing_app.data.collection.remote.dto

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CollectionSummaryDTOTest {

    @Test
    fun `toDomain should map all fields correctly`() {
        val dto = CollectionSummaryDTO(
            id = "collection-123",
            name = "Fantasy Classics",
            readingLevel = "ADULT",
            primaryLanguage = "ENGLISH",
            primaryGenre = "FANTASY"
        )

        val collection = dto.toDomain()

        assertEquals(dto.id, collection.id)
        assertEquals(dto.name, collection.name)
        assertEquals(dto.readingLevel, collection.readingLevel.toString())
        assertEquals(dto.primaryLanguage, collection.primaryLanguage.toString())
        assertEquals(dto.primaryGenre, collection.primaryGenre.toString())
    }

    @Test
    fun `toDomain should return empty strings when optional fields are null`() {
        val dto = CollectionSummaryDTO(
            id = "collection-123",
            name = "Fantasy Classics",
            readingLevel = null,
            primaryLanguage = null,
            primaryGenre = null
        )

        val collection = dto.toDomain()

        assertNull(collection.readingLevel)
        assertNull(collection.primaryLanguage)
        assertNull(collection.primaryGenre)
    }
}
