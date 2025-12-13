package org.cescfe.book_publishing_app.ui.collections.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.cescfe.book_publishing_app.domain.collection.model.CollectionSummary
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme

@Preview(showBackground = true)
@Composable
private fun CollectionSummaryCardPreview() {
    BookpublishingappTheme {
        CollectionSummaryCard(
            collectionSummary = CollectionSummary(
                id = "1",
                name = "Fantasy Collection",
                readingLevel = "Advanced",
                primaryLanguage = "English",
                primaryGenre = "Fantasy"
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CollectionSummaryCardNoReadingLevelPreview() {
    BookpublishingappTheme {
        CollectionSummaryCard(
            collectionSummary = CollectionSummary(
                id = "2",
                name = "Classic Literature",
                readingLevel = null,
                primaryLanguage = "Spanish",
                primaryGenre = "Classics"
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CollectionSummaryCardNoLanguagePreview() {
    BookpublishingappTheme {
        CollectionSummaryCard(
            collectionSummary = CollectionSummary(
                id = "3",
                name = "Science Fiction",
                readingLevel = "Intermediate",
                primaryLanguage = null,
                primaryGenre = "Sci-Fi"
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CollectionSummaryCardNoGenrePreview() {
    BookpublishingappTheme {
        CollectionSummaryCard(
            collectionSummary = CollectionSummary(
                id = "4",
                name = "Mixed Collection",
                readingLevel = "Beginner",
                primaryLanguage = "Catalan",
                primaryGenre = null
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CollectionSummaryCardMinimalPreview() {
    BookpublishingappTheme {
        CollectionSummaryCard(
            collectionSummary = CollectionSummary(
                id = "5",
                name = "Unnamed Collection",
                readingLevel = null,
                primaryLanguage = null,
                primaryGenre = null
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CollectionSummaryCardLongNamePreview() {
    BookpublishingappTheme {
        CollectionSummaryCard(
            collectionSummary = CollectionSummary(
                id = "6",
                name = "A Very Long Collection Name That Might Overflow The Card Layout",
                readingLevel = "Advanced Reader Level",
                primaryLanguage = "English (United States)",
                primaryGenre = "Historical Fiction"
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}
