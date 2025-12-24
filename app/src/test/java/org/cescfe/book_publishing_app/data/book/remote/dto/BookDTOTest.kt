package org.cescfe.book_publishing_app.data.book.remote.dto

import kotlinx.serialization.json.Json
import org.cescfe.book_publishing_app.domain.book.model.enums.Status
import org.cescfe.book_publishing_app.domain.shared.enums.Genre
import org.cescfe.book_publishing_app.domain.shared.enums.Language
import org.cescfe.book_publishing_app.domain.shared.enums.ReadingLevel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class BookDTOTest {

    @Test
    fun `toDomain should map all fields correctly`() {
        val dto = BookDTO(
            id = "1b871121-881e-44cf-b7b8-a2e9954ce6d6",
            title = "Harry Potter and the Deathly Hallows",
            basePrice = 29.99,
            author = AuthorRefDTO(
                id = "83e8e00b-31f9-4ab6-9c07-4b0689aaddf2",
                name = "Joanne Rowling"
            ),
            collection = CollectionRefDTO(
                id = "a7ca1a05-57e5-45f0-8bb1-0300f20942ec",
                name = "Young Wizards"
            ),
            readingLevel = "YOUNG_ADULT",
            primaryLanguage = "ENGLISH",
            secondaryLanguages = listOf("SPANISH", "CATALAN"),
            primaryGenre = "FANTASY",
            secondaryGenres = listOf("ADVENTURE", "MYSTERY"),
            vatRate = 0.04,
            finalPrice = 31.19,
            isbn = "9780747591054",
            publicationDate = "2007-07-21",
            pageCount = 607,
            description = "Harry, Ron, and Hermione hunt for Horcruxes",
            status = Status.DRAFT
        )

        val book = dto.toDomain()

        assertEquals(dto.id, book.id)
        assertEquals(dto.title, book.title)
        assertEquals(dto.basePrice, book.basePrice, 0.001)
        assertEquals(dto.author.name, book.authorName)
        assertEquals(dto.collection.name, book.collectionName)
        assertEquals(ReadingLevel.YOUNG_ADULT, book.readingLevel)
        assertEquals(Language.ENGLISH, book.primaryLanguage)
        assertEquals(2, book.secondaryLanguages.size)
        assertEquals(Language.SPANISH, book.secondaryLanguages[0])
        assertEquals(Language.CATALAN, book.secondaryLanguages[1])
        assertEquals(Genre.FANTASY, book.primaryGenre)
        assertEquals(2, book.secondaryGenres.size)
        assertEquals(Genre.ADVENTURE, book.secondaryGenres[0])
        assertEquals(Genre.MYSTERY, book.secondaryGenres[1])
        assertEquals(dto.vatRate, book.vatRate, 0.001)
        assertEquals(dto.finalPrice, book.finalPrice, 0.001)
        assertEquals(dto.isbn, book.isbn)
        assertEquals(dto.publicationDate, book.publicationDate)
        assertEquals(dto.pageCount, book.pageCount)
        assertEquals(dto.description, book.description)
        assertEquals(Status.DRAFT, book.status)
    }

    @Test
    fun `toDomain should handle nullable collection`() {
        val dto = BookDTO(
            id = "1",
            title = "Test Book",
            basePrice = 10.0,
            author = AuthorRefDTO(id = "author-1", name = "Author Name"),
            collection = CollectionRefDTO(id = "collection-1", name = "Collection Name"),
            readingLevel = null,
            primaryLanguage = null,
            secondaryLanguages = emptyList(),
            primaryGenre = null,
            secondaryGenres = emptyList(),
            vatRate = 0.04,
            finalPrice = 10.04,
            isbn = null,
            publicationDate = null,
            pageCount = null,
            description = null,
            status = Status.PUBLISHED
        )

        val book = dto.toDomain()

        assertEquals("Collection Name", book.collectionName)
        assertNull(book.readingLevel)
        assertNull(book.primaryLanguage)
        assertEquals(0, book.secondaryLanguages.size)
        assertNull(book.primaryGenre)
        assertEquals(0, book.secondaryGenres.size)
        assertNull(book.isbn)
        assertNull(book.publicationDate)
        assertNull(book.pageCount)
        assertNull(book.description)
    }

    @Test
    fun `BookDTO should deserialize from JSON correctly`() {
        val json = """
        {
            "id": "1b871121-881e-44cf-b7b8-a2e9954ce6d6",
            "title": "Harry Potter and the Deathly Hallows",
            "base_price": 29.99,
            "author": {
                "id": "83e8e00b-31f9-4ab6-9c07-4b0689aaddf2",
                "name": "Joanne Rowling"
            },
            "collection": {
                "id": "a7ca1a05-57e5-45f0-8bb1-0300f20942ec",
                "name": "Young Wizards"
            },
            "reading_level": "YOUNG_ADULT",
            "primary_language": "ENGLISH",
            "secondary_languages": ["SPANISH", "CATALAN"],
            "primary_genre": "FANTASY",
            "secondary_genres": ["ADVENTURE", "MYSTERY"],
            "vat_rate": 0.04,
            "final_price": 31.19,
            "isbn": "9780747591054",
            "publication_date": "2007-07-21",
            "page_count": 607,
            "description": "Harry, Ron, and Hermione hunt for Horcruxes",
            "status": "DRAFT"
        }
        """.trimIndent()

        val dto = Json.decodeFromString<BookDTO>(json)

        assertEquals("1b871121-881e-44cf-b7b8-a2e9954ce6d6", dto.id)
        assertEquals("Harry Potter and the Deathly Hallows", dto.title)
        assertEquals(29.99, dto.basePrice, 0.001)
        assertEquals("Joanne Rowling", dto.author.name)
        assertEquals("Young Wizards", dto.collection.name)
        assertEquals("YOUNG_ADULT", dto.readingLevel)
        assertEquals("ENGLISH", dto.primaryLanguage)
        assertEquals(2, dto.secondaryLanguages.size)
        assertEquals("FANTASY", dto.primaryGenre)
        assertEquals(2, dto.secondaryGenres.size)
        assertEquals(0.04, dto.vatRate, 0.001)
        assertEquals(31.19, dto.finalPrice, 0.001)
        assertEquals("9780747591054", dto.isbn)
        assertEquals("2007-07-21", dto.publicationDate)
        assertEquals(607, dto.pageCount)
        assertEquals("Harry, Ron, and Hermione hunt for Horcruxes", dto.description)
        assertEquals(Status.DRAFT, dto.status)
    }
}
