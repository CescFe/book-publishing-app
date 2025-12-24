package org.cescfe.book_publishing_app.ui.book.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale
import org.cescfe.book_publishing_app.R
import org.cescfe.book_publishing_app.domain.book.model.Book
import org.cescfe.book_publishing_app.domain.book.model.enums.Status
import org.cescfe.book_publishing_app.domain.shared.enums.Genre
import org.cescfe.book_publishing_app.domain.shared.enums.Language
import org.cescfe.book_publishing_app.domain.shared.enums.ReadingLevel

@Composable
fun BookCard(book: Book, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("book_card_${book.id}"),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.book_2),
                    contentDescription = stringResource(R.string.book_card_icon_description),
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = book.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("book_card_title"),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            BookInfoRow(
                label = stringResource(R.string.book_card_author_label),
                value = book.authorName,
                testTag = "book_card_author"
            )

            BookInfoRow(
                label = stringResource(R.string.book_card_collection_label),
                value = book.collectionName,
                testTag = "book_card_collection"
            )

            BookInfoRow(
                label = stringResource(R.string.book_card_isbn_label),
                value = book.isbn,
                testTag = "book_card_isbn"
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(R.string.book_card_publication_date_label),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = formatPublicationDate(book.publicationDate) ?: stringResource(R.string.not_informed),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.testTag("book_card_publication_date")
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(R.string.book_card_page_count_label),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = if (book.pageCount != null) {
                        stringResource(R.string.book_card_page_count, book.pageCount)
                    } else {
                        stringResource(R.string.not_informed)
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.testTag("book_card_page_count")
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(R.string.book_card_reading_level_label),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = formatReadingLevel(book.readingLevel) ?: stringResource(R.string.not_informed),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.testTag("book_card_reading_level")
                )
            }

            BookInfoRow(
                label = stringResource(R.string.book_card_primary_language_label),
                value = book.primaryLanguage?.let { formatLanguage(it) },
                testTag = "book_card_primary_language"
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(R.string.book_card_secondary_languages_label),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = formatLanguages(book.secondaryLanguages) ?: stringResource(R.string.not_informed),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.testTag("book_card_secondary_languages")
                )
            }

            BookInfoRow(
                label = stringResource(R.string.book_card_primary_genre_label),
                value = book.primaryGenre?.displayName,
                testTag = "book_card_primary_genre"
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(R.string.book_card_secondary_genres_label),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = formatGenres(book.secondaryGenres) ?: stringResource(R.string.not_informed),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.testTag("book_card_secondary_genres")
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(R.string.book_card_base_price_label),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = formatPrice(book.basePrice),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.testTag("book_card_base_price")
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(R.string.book_card_final_price_label),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = formatPrice(book.finalPrice),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.testTag("book_card_final_price")
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(R.string.book_card_description_label),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = book.description?.takeIf { it.isNotBlank() } ?: stringResource(R.string.not_informed),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.testTag("book_card_description"),
                    maxLines = 10,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(R.string.book_card_status_label),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = formatStatus(book.status),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.testTag("book_card_status")
                )
            }
        }
    }
}

@Composable
private fun BookInfoRow(
    label: String,
    value: String?,
    modifier: Modifier = Modifier,
    testTag: String? = null
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value?.takeIf { it.isNotBlank() } ?: stringResource(R.string.not_informed),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = testTag?.let { Modifier.testTag(it) } ?: Modifier,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

private fun formatPrice(price: Double): String {
    val numberFormat = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-ES"))
    return numberFormat.format(price)
}

private fun formatPublicationDate(dateString: String?): String? {
    if (dateString.isNullOrBlank()) {
        return null
    }
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = inputFormat.parse(dateString) ?: return dateString

        val locale = Locale.getDefault()
        val language = locale.language

        if (language == "es" || language == "ca") {
            val dayFormat = SimpleDateFormat("d", locale)
            val monthFormat = SimpleDateFormat("MMMM", locale)
            val yearFormat = SimpleDateFormat("yyyy", locale)

            val day = dayFormat.format(date)
            val month = monthFormat.format(date)
            val year = yearFormat.format(date)

            "$day de $month de $year"
        } else {
            val outputFormat = SimpleDateFormat("MMMM dd, yyyy", locale)
            outputFormat.format(date)
        }
    } catch (_: Exception) {
        dateString
    }
}

private fun formatReadingLevel(readingLevel: ReadingLevel?): String? {
    return when (readingLevel) {
        ReadingLevel.CHILDREN -> "Children"
        ReadingLevel.YOUNG_ADULT -> "Young Adult"
        ReadingLevel.ADULT -> "Adult"
        null -> null
    }
}

private fun formatLanguage(language: Language): String {
    return when (language) {
        Language.CATALAN -> "Catalan"
        Language.VALENCIAN -> "Valencian"
        Language.SPANISH -> "Spanish"
        Language.ENGLISH -> "English"
    }
}

private fun formatLanguages(languages: List<Language?>): String? {
    val formatted = languages
        .filterNotNull()
        .joinToString(", ") { formatLanguage(it) }
    return formatted.takeIf { it.isNotEmpty() }
}

private fun formatGenres(genres: List<Genre?>): String? {
    val formatted = genres
        .filterNotNull()
        .joinToString(", ") { it.displayName }
    return formatted.takeIf { it.isNotEmpty() }
}

private fun formatStatus(status: Status): String {
    return when (status) {
        Status.DRAFT -> "Draft"
        Status.PUBLISHED -> "Published"
        Status.OUT_OF_PRINT -> "Out of Print"
        Status.DISCONTINUED -> "Discontinued"
    }
}
