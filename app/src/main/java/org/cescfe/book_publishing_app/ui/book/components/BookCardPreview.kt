package org.cescfe.book_publishing_app.ui.book.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.cescfe.book_publishing_app.domain.book.model.Book
import org.cescfe.book_publishing_app.domain.book.model.enums.Status
import org.cescfe.book_publishing_app.domain.shared.enums.Genre
import org.cescfe.book_publishing_app.domain.shared.enums.Language
import org.cescfe.book_publishing_app.domain.shared.enums.ReadingLevel
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme

@Preview(showBackground = true)
@Composable
private fun BookCardPreview() {
    BookpublishingappTheme {
        BookCard(
            book = Book(
                id = "1",
                title = "Harry Potter and the Deathly Hallows",
                basePrice = 29.99,
                authorName = "J.K. Rowling",
                collectionName = "Harry Potter Series",
                readingLevel = ReadingLevel.YOUNG_ADULT,
                primaryLanguage = Language.ENGLISH,
                secondaryLanguages = listOf(Language.SPANISH, Language.CATALAN),
                primaryGenre = Genre.FANTASY,
                secondaryGenres = listOf(Genre.ADVENTURE, Genre.MYSTERY),
                vatRate = 0.04,
                finalPrice = 31.19,
                isbn = "9780747591054",
                publicationDate = "2007-07-21",
                pageCount = 607,
                description = "Harry, Ron, and Hermione hunt for Horcruxes",
                status = Status.PUBLISHED
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookCardMinimalPreview() {
    BookpublishingappTheme {
        BookCard(
            book = Book(
                id = "2",
                title = "Minimal Book",
                basePrice = 10.0,
                authorName = "Author Name",
                collectionName = "Collection",
                readingLevel = null,
                primaryLanguage = null,
                secondaryLanguages = emptyList(),
                primaryGenre = null,
                secondaryGenres = emptyList(),
                vatRate = 0.04,
                finalPrice = 10.40,
                isbn = null,
                publicationDate = null,
                pageCount = null,
                description = null,
                status = Status.DRAFT
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}
