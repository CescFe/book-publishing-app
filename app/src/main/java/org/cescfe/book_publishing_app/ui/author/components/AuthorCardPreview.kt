package org.cescfe.book_publishing_app.ui.author.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.cescfe.book_publishing_app.domain.author.model.Author
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme

@Preview(showBackground = true)
@Composable
private fun AuthorCardPreview() {
    BookpublishingappTheme {
        AuthorCard(
            author = Author(
                id = "1",
                fullName = "J.R.R. Tolkien",
                pseudonym = "Tolkien",
                biography = "John Ronald Reuel Tolkien was an English writer and philologist. " +
                    "He was the author of the high fantasy works The Hobbit and The Lord of the Rings.",
                email = "tolkien@example.com",
                website = "https://www.tolkienestate.com"
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthorCardNoOptionalFieldsPreview() {
    BookpublishingappTheme {
        AuthorCard(
            author = Author(
                id = "2",
                fullName = "George Orwell",
                pseudonym = null,
                biography = null,
                email = null,
                website = null
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthorCardPartialFieldsPreview() {
    BookpublishingappTheme {
        AuthorCard(
            author = Author(
                id = "3",
                fullName = "Jane Austen",
                pseudonym = "A Lady",
                biography = "Jane Austen was an English novelist known primarily for her six major novels.",
                email = null,
                website = "https://www.janeausten.org"
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthorCardLongContentPreview() {
    BookpublishingappTheme {
        AuthorCard(
            author = Author(
                id = "4",
                fullName = "A Very Long Author Name That Might Overflow The Card Layout",
                pseudonym = "Very Long Pseudonym That Could Also Overflow",
                biography = "This is a very long biography that contains multiple sentences and paragraphs. " +
                    "It describes the author's life, works, and achievements in great detail. " +
                    "The biography might be so long that it needs to be truncated with ellipsis. " +
                    "This ensures that the card layout remains clean and readable even with extensive content.",
                email = "verylongemailaddress@example.com",
                website = "https://www.verylongwebsitename.com/very/long/path/to/page"
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthorCardInvalidWebsitePreview() {
    BookpublishingappTheme {
        AuthorCard(
            author = Author(
                id = "5",
                fullName = "Test Author",
                pseudonym = null,
                biography = "Test biography",
                email = "test@example.com",
                website = "not-a-valid-url"
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}
