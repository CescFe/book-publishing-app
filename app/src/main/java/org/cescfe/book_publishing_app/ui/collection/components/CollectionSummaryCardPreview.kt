package org.cescfe.book_publishing_app.ui.collection.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.cescfe.book_publishing_app.domain.collection.model.CollectionSummary
import org.cescfe.book_publishing_app.domain.shared.enums.Genre
import org.cescfe.book_publishing_app.domain.shared.enums.Language
import org.cescfe.book_publishing_app.domain.shared.enums.ReadingLevel
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme

@Preview(showBackground = true)
@Composable
private fun CollectionSummaryCardPreview() {
    BookpublishingappTheme {
        CollectionSummaryCard(
            collectionSummary = CollectionSummary(
                id = "1",
                name = "Fantasy Collection",
                readingLevel = ReadingLevel.ADULT,
                primaryLanguage = Language.ENGLISH,
                primaryGenre = Genre.FANTASY
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
                primaryLanguage = Language.VALENCIAN,
                primaryGenre = Genre.GENERAL_INTEREST
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
                readingLevel = ReadingLevel.YOUNG_ADULT,
                primaryLanguage = null,
                primaryGenre = Genre.SCIENCE_FICTION
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
                readingLevel = ReadingLevel.CHILDREN,
                primaryLanguage = Language.CATALAN,
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
                readingLevel = ReadingLevel.CHILDREN,
                primaryLanguage = Language.ENGLISH,
                primaryGenre = Genre.HISTORICAL_FICTION
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}
