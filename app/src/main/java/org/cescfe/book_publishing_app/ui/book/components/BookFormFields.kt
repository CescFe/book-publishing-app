package org.cescfe.book_publishing_app.ui.book.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.cescfe.book_publishing_app.R
import org.cescfe.book_publishing_app.domain.book.model.enums.Status
import org.cescfe.book_publishing_app.domain.shared.enums.Genre
import org.cescfe.book_publishing_app.domain.shared.enums.Language
import org.cescfe.book_publishing_app.domain.shared.enums.ReadingLevel
import org.cescfe.book_publishing_app.domain.shared.toLocalizedString
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme

@Composable
fun BookFormFields(
    modifier: Modifier = Modifier,
    title: String,
    onTitleChange: (String) -> Unit,
    authorId: String,
    onAuthorIdChange: (String) -> Unit,
    collectionId: String,
    onCollectionIdChange: (String) -> Unit,
    basePrice: String,
    onBasePriceChange: (String) -> Unit,
    readingLevel: ReadingLevel?,
    onReadingLevelChange: (ReadingLevel?) -> Unit,
    primaryLanguage: Language?,
    onPrimaryLanguageChange: (Language?) -> Unit,
    secondaryLanguages: List<Language>,
    onSecondaryLanguagesChange: (List<Language>) -> Unit,
    primaryGenre: Genre?,
    onPrimaryGenreChange: (Genre?) -> Unit,
    secondaryGenres: List<Genre>,
    onSecondaryGenresChange: (List<Genre>) -> Unit,
    vatRate: String,
    onVatRateChange: (String) -> Unit,
    isbn: String,
    onIsbnChange: (String) -> Unit,
    publicationDate: String,
    onPublicationDateChange: (String) -> Unit,
    pageCount: String,
    onPageCountChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    status: Status?,
    onStatusChange: (Status?) -> Unit,
    titleError: String? = null,
    authorIdError: String? = null,
    collectionIdError: String? = null,
    basePriceError: String? = null,
    vatRateError: String? = null,
    isbnError: String? = null,
    publicationDateError: String? = null,
    pageCountError: String? = null,
    descriptionError: String? = null,
    secondaryLanguagesError: String? = null,
    secondaryGenresError: String? = null,
    enabled: Boolean = true
) {
    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text(stringResource(R.string.book_title_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("title_field"),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            enabled = enabled,
            isError = titleError != null,
            supportingText = titleError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } }
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = authorId,
            onValueChange = onAuthorIdChange,
            label = { Text(stringResource(R.string.book_author_id_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("author_id_field"),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            enabled = enabled,
            isError = authorIdError != null,
            supportingText = authorIdError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } }
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = collectionId,
            onValueChange = onCollectionIdChange,
            label = { Text(stringResource(R.string.book_collection_id_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("collection_id_field"),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            enabled = enabled,
            isError = collectionIdError != null,
            supportingText = collectionIdError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } }
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = basePrice,
            onValueChange = onBasePriceChange,
            label = { Text(stringResource(R.string.book_base_price_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("base_price_field"),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next
            ),
            enabled = enabled,
            isError = basePriceError != null,
            supportingText = basePriceError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } }
        )

        Spacer(modifier = Modifier.height(12.dp))

        ReadingLevelDropdown(
            selectedValue = readingLevel,
            onValueChange = onReadingLevelChange,
            enabled = enabled
        )

        Spacer(modifier = Modifier.height(12.dp))

        LanguageDropdown(
            selectedValue = primaryLanguage,
            onValueChange = onPrimaryLanguageChange,
            label = stringResource(R.string.book_primary_language_label),
            enabled = enabled
        )

        Spacer(modifier = Modifier.height(12.dp))

        SecondaryLanguagesMultiSelect(
            selectedLanguages = secondaryLanguages,
            onLanguagesChange = onSecondaryLanguagesChange,
            primaryLanguage = primaryLanguage,
            error = secondaryLanguagesError,
            enabled = enabled
        )

        Spacer(modifier = Modifier.height(12.dp))

        GenreDropdown(
            selectedValue = primaryGenre,
            onValueChange = onPrimaryGenreChange,
            label = stringResource(R.string.book_primary_genre_label),
            enabled = enabled
        )

        Spacer(modifier = Modifier.height(12.dp))

        SecondaryGenresMultiSelect(
            selectedGenres = secondaryGenres,
            onGenresChange = onSecondaryGenresChange,
            primaryGenre = primaryGenre,
            error = secondaryGenresError,
            enabled = enabled
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = vatRate,
            onValueChange = onVatRateChange,
            label = { Text(stringResource(R.string.book_vat_rate_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("vat_rate_field"),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next
            ),
            enabled = enabled,
            isError = vatRateError != null,
            supportingText = vatRateError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } }
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = isbn,
            onValueChange = onIsbnChange,
            label = { Text(stringResource(R.string.book_isbn_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("isbn_field"),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            enabled = enabled,
            isError = isbnError != null,
            supportingText = isbnError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } }
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = publicationDate,
            onValueChange = onPublicationDateChange,
            label = { Text(stringResource(R.string.book_publication_date_label)) },
            placeholder = { Text(stringResource(R.string.book_publication_date_hint)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("publication_date_field"),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            enabled = enabled,
            isError = publicationDateError != null,
            supportingText = publicationDateError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } }
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = pageCount,
            onValueChange = onPageCountChange,
            label = { Text(stringResource(R.string.book_page_count_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("page_count_field"),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            enabled = enabled,
            isError = pageCountError != null,
            supportingText = pageCountError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } }
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text(stringResource(R.string.book_description_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("description_field"),
            maxLines = 4,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            enabled = enabled,
            isError = descriptionError != null,
            supportingText = descriptionError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } }
        )

        Spacer(modifier = Modifier.height(12.dp))

        StatusDropdown(
            selectedValue = status,
            onValueChange = onStatusChange,
            enabled = enabled
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReadingLevelDropdown(
    selectedValue: ReadingLevel?,
    onValueChange: (ReadingLevel?) -> Unit,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }
    val options = ReadingLevel.entries

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier
            .fillMaxWidth()
            .testTag("reading_level_dropdown")
    ) {
        OutlinedTextField(
            value = selectedValue?.toLocalizedString() ?: stringResource(R.string.select_none),
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.book_reading_level_label)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = enabled),
            enabled = enabled
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.select_none)) },
                onClick = {
                    onValueChange(null)
                    expanded = false
                }
            )
            options.forEach { level ->
                DropdownMenuItem(
                    text = { Text(level.toLocalizedString()) },
                    onClick = {
                        onValueChange(level)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LanguageDropdown(
    selectedValue: Language?,
    onValueChange: (Language?) -> Unit,
    label: String,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }
    val options = Language.entries

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier
            .fillMaxWidth()
            .testTag("language_dropdown")
    ) {
        OutlinedTextField(
            value = selectedValue?.toLocalizedString() ?: stringResource(R.string.select_none),
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = enabled),
            enabled = enabled
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.select_none)) },
                onClick = {
                    onValueChange(null)
                    expanded = false
                }
            )
            options.forEach { language ->
                DropdownMenuItem(
                    text = { Text(language.toLocalizedString()) },
                    onClick = {
                        onValueChange(language)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GenreDropdown(
    selectedValue: Genre?,
    onValueChange: (Genre?) -> Unit,
    label: String,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }
    val options = Genre.entries

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier
            .fillMaxWidth()
            .testTag("genre_dropdown")
    ) {
        OutlinedTextField(
            value = selectedValue?.toLocalizedString() ?: stringResource(R.string.select_none),
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = enabled),
            enabled = enabled
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.select_none)) },
                onClick = {
                    onValueChange(null)
                    expanded = false
                }
            )
            options.forEach { genre ->
                DropdownMenuItem(
                    text = { Text(genre.toLocalizedString()) },
                    onClick = {
                        onValueChange(genre)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatusDropdown(selectedValue: Status?, onValueChange: (Status?) -> Unit, enabled: Boolean = true) {
    var expanded by remember { mutableStateOf(false) }
    val options = Status.entries

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier
            .fillMaxWidth()
            .testTag("status_dropdown")
    ) {
        OutlinedTextField(
            value = selectedValue?.toLocalizedString() ?: stringResource(R.string.select_none),
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.book_status_label)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = enabled),
            enabled = enabled
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.select_none)) },
                onClick = {
                    onValueChange(null)
                    expanded = false
                }
            )
            options.forEach { status ->
                DropdownMenuItem(
                    text = { Text(status.toLocalizedString()) },
                    onClick = {
                        onValueChange(status)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SecondaryLanguagesMultiSelect(
    selectedLanguages: List<Language>,
    onLanguagesChange: (List<Language>) -> Unit,
    primaryLanguage: Language?,
    error: String?,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }
    val availableLanguages = Language.entries
        .filter { it != primaryLanguage }

    Column {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("secondary_languages_dropdown")
        ) {
            OutlinedTextField(
                value = if (selectedLanguages.isEmpty()) {
                    stringResource(R.string.select_none)
                } else {
                    selectedLanguages.map { it.toLocalizedString() }.joinToString(", ")
                },
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.book_secondary_languages_label)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = enabled),
                enabled = enabled,
                isError = error != null,
                supportingText = error?.let { { Text(it, color = MaterialTheme.colorScheme.error) } }
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                availableLanguages.forEach { language ->
                    val isSelected = selectedLanguages.contains(language)
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = language.toLocalizedString(),
                                style = if (isSelected) {
                                    MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                                    )
                                } else {
                                    MaterialTheme.typography.bodyLarge
                                }
                            )
                        },
                        onClick = {
                            val newList = if (isSelected) {
                                selectedLanguages - language
                            } else {
                                if (selectedLanguages.size < 3) {
                                    selectedLanguages + language
                                } else {
                                    selectedLanguages // Max 3
                                }
                            }
                            onLanguagesChange(newList)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SecondaryGenresMultiSelect(
    selectedGenres: List<Genre>,
    onGenresChange: (List<Genre>) -> Unit,
    primaryGenre: Genre?,
    error: String?,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }
    val availableGenres = Genre.entries
        .filter { it != primaryGenre }

    Column {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("secondary_genres_dropdown")
        ) {
            OutlinedTextField(
                value = if (selectedGenres.isEmpty()) {
                    stringResource(R.string.select_none)
                } else {
                    selectedGenres.map { it.toLocalizedString() }.joinToString(", ")
                },
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.book_secondary_genres_label)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = enabled),
                enabled = enabled,
                isError = error != null,
                supportingText = error?.let { { Text(it, color = MaterialTheme.colorScheme.error) } }
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                availableGenres.forEach { genre ->
                    val isSelected = selectedGenres.contains(genre)
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = genre.toLocalizedString(),
                                style = if (isSelected) {
                                    MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                                    )
                                } else {
                                    MaterialTheme.typography.bodyLarge
                                }
                            )
                        },
                        onClick = {
                            val newList = if (isSelected) {
                                selectedGenres - genre
                            } else {
                                if (selectedGenres.size < 3) {
                                    selectedGenres + genre
                                } else {
                                    selectedGenres // Max 3
                                }
                            }
                            onGenresChange(newList)
                        }
                    )
                }
            }
        }
    }
}

// ==================== PREVIEWS ====================

@Preview(showBackground = true)
@Composable
private fun BookFormFieldsEmptyPreview() {
    BookpublishingappTheme {
        BookFormFields(
            title = "",
            onTitleChange = {},
            authorId = "",
            onAuthorIdChange = {},
            collectionId = "",
            onCollectionIdChange = {},
            basePrice = "",
            onBasePriceChange = {},
            readingLevel = null,
            onReadingLevelChange = {},
            primaryLanguage = null,
            onPrimaryLanguageChange = {},
            secondaryLanguages = emptyList(),
            onSecondaryLanguagesChange = {},
            primaryGenre = null,
            onPrimaryGenreChange = {},
            secondaryGenres = emptyList(),
            onSecondaryGenresChange = {},
            vatRate = "",
            onVatRateChange = {},
            isbn = "",
            onIsbnChange = {},
            publicationDate = "",
            onPublicationDateChange = {},
            pageCount = "",
            onPageCountChange = {},
            description = "",
            onDescriptionChange = {},
            status = null,
            onStatusChange = {}
        )
    }
}
