package org.cescfe.book_publishing_app.ui.books.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.cescfe.book_publishing_app.domain.book.model.BookSummary
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme

@Preview(showBackground = true)
@Composable
private fun BookSummaryCardPreview() {
    BookpublishingappTheme {
        BookSummaryCard(
            bookSummary = BookSummary(
                id = "1",
                title = "The Lord of the Rings",
                author = "J.R.R. Tolkien",
                collection = "Fantasy Collection",
                finalPrice = 29.99,
                isbn = "978-0-544-00001-0"
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookSummaryCardNoIsbnPreview() {
    BookpublishingappTheme {
        BookSummaryCard(
            bookSummary = BookSummary(
                id = "2",
                title = "A Very Long Book Title That Might Overflow",
                author = "Author Name",
                collection = "",
                finalPrice = 15.50,
                isbn = null
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookSummaryCardNoCollectionPreview() {
    BookpublishingappTheme {
        BookSummaryCard(
            bookSummary = BookSummary(
                id = "3",
                title = "Standalone Book",
                author = "Standalone Author",
                collection = "",
                finalPrice = 9.99,
                isbn = "978-0-123456-78-9"
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}
