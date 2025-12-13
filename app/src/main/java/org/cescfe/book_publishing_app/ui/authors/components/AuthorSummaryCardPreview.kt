package org.cescfe.book_publishing_app.ui.authors.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.cescfe.book_publishing_app.domain.author.model.AuthorSummary
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme

@Preview(showBackground = true)
@Composable
private fun AuthorSummaryCardPreview() {
    BookpublishingappTheme {
        AuthorSummaryCard(
            authorSummary = AuthorSummary(
                id = "1",
                fullName = "J.R.R. Tolkien",
                pseudonym = "Tolkien",
                email = "tolkien@example.com"
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthorSummaryCardNoPseudonymPreview() {
    BookpublishingappTheme {
        AuthorSummaryCard(
            authorSummary = AuthorSummary(
                id = "2",
                fullName = "George Orwell",
                pseudonym = null,
                email = "orwell@example.com"
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthorSummaryCardNoEmailPreview() {
    BookpublishingappTheme {
        AuthorSummaryCard(
            authorSummary = AuthorSummary(
                id = "3",
                fullName = "Jane Austen",
                pseudonym = "A Lady",
                email = null
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthorSummaryCardNoPseudonymNoEmailPreview() {
    BookpublishingappTheme {
        AuthorSummaryCard(
            authorSummary = AuthorSummary(
                id = "4",
                fullName = "Anonymous Author",
                pseudonym = null,
                email = null
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthorSummaryCardLongNamePreview() {
    BookpublishingappTheme {
        AuthorSummaryCard(
            authorSummary = AuthorSummary(
                id = "5",
                fullName = "A Very Long Author Name That Might Overflow The Card Layout",
                pseudonym = "Long Pseudonym",
                email = "verylongemailaddress@example.com"
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}