package org.cescfe.book_publishing_app.ui.author.components

import android.content.Intent
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import org.cescfe.book_publishing_app.R
import org.cescfe.book_publishing_app.domain.author.model.Author

@Composable
fun AuthorCard(author: Author, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val websiteErrorText = stringResource(R.string.author_card_website_open_error)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("author_card_${author.id}"),
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
                    painter = painterResource(id = R.drawable.ic_person),
                    contentDescription = stringResource(R.string.author_card_icon_description),
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = author.fullName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            AuthorInfoRow(
                label = stringResource(R.string.author_card_pseudonym_label),
                value = author.pseudonym,
                emptyValueRes = R.string.author_card_no_pseudonym,
                testTag = "author_card_pseudonym"
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(R.string.author_card_biography_label),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = if (author.biography.isNullOrBlank()) {
                        stringResource(R.string.author_card_no_biography)
                    } else {
                        author.biography
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.testTag("author_card_biography"),
                    maxLines = 10,
                    overflow = TextOverflow.Ellipsis
                )
            }

            AuthorInfoRow(
                label = stringResource(R.string.author_card_email_label),
                value = author.email,
                emptyValueRes = R.string.author_card_no_email,
                testTag = "author_card_email"
            )

            val website = author.website
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(R.string.author_card_website_label),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (website.isNullOrBlank()) {
                    Text(
                        text = stringResource(R.string.author_card_no_website),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.testTag("author_card_website")
                    )
                } else {
                    val isValidUrl = isValidUrl(website)
                    Text(
                        text = website,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isValidUrl) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        },
                        modifier = Modifier
                            .testTag("author_card_website")
                            .then(
                                if (isValidUrl) {
                                    Modifier.clickable {
                                        try {
                                            val intent = Intent(Intent.ACTION_VIEW, website.toUri())
                                            context.startActivity(intent)
                                        } catch (_: Exception) {
                                            Toast.makeText(
                                                context,
                                                websiteErrorText,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                } else {
                                    Modifier
                                }
                            ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun AuthorInfoRow(
    label: String,
    value: String?,
    @StringRes emptyValueRes: Int,
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
            text = if (value.isNullOrBlank()) {
                stringResource(emptyValueRes)
            } else {
                value
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = testTag?.let { Modifier.testTag(it) } ?: Modifier,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

private fun isValidUrl(url: String): Boolean = try {
    val uri = url.toUri()
    uri.scheme != null && (uri.scheme == "http" || uri.scheme == "https")
} catch (_: Exception) {
    false
}
