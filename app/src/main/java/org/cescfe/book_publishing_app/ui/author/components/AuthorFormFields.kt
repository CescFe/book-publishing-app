package org.cescfe.book_publishing_app.ui.author.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.cescfe.book_publishing_app.R
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme

@Composable
fun AuthorFormFields(
    modifier: Modifier = Modifier,
    fullName: String,
    onFullNameChange: (String) -> Unit,
    pseudonym: String,
    onPseudonymChange: (String) -> Unit,
    biography: String,
    onBiographyChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    website: String,
    onWebsiteChange: (String) -> Unit,
    fullNameError: String? = null,
    enabled: Boolean = true
) {
    Column(modifier = modifier) {
        // Full Name (required)
        OutlinedTextField(
            value = fullName,
            onValueChange = onFullNameChange,
            label = { Text(stringResource(R.string.author_full_name_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("full_name_field"),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            enabled = enabled,
            isError = fullNameError != null,
            supportingText = if (fullNameError != null) {
                { Text(text = fullNameError, color = MaterialTheme.colorScheme.error) }
            } else {
                null
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Pseudonym (optional)
        OutlinedTextField(
            value = pseudonym,
            onValueChange = onPseudonymChange,
            label = { Text(stringResource(R.string.author_pseudonym_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("pseudonym_field"),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            enabled = enabled
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Biography (optional, multiline)
        OutlinedTextField(
            value = biography,
            onValueChange = onBiographyChange,
            label = { Text(stringResource(R.string.author_biography_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("biography_field"),
            maxLines = 4,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            enabled = enabled
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Email (optional)
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text(stringResource(R.string.author_email_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("email_field"),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            enabled = enabled
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Website (optional)
        OutlinedTextField(
            value = website,
            onValueChange = onWebsiteChange,
            label = { Text(stringResource(R.string.author_website_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("website_field"),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Done
            ),
            enabled = enabled
        )
    }
}

// ==================== PREVIEWS ====================

@Preview(showBackground = true)
@Composable
private fun AuthorFormFieldsEmptyPreview() {
    BookpublishingappTheme {
        AuthorFormFields(
            fullName = "",
            onFullNameChange = {},
            pseudonym = "",
            onPseudonymChange = {},
            biography = "",
            onBiographyChange = {},
            email = "",
            onEmailChange = {},
            website = "",
            onWebsiteChange = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthorFormFieldsWithDataPreview() {
    BookpublishingappTheme {
        AuthorFormFields(
            fullName = "J.R.R. Tolkien",
            onFullNameChange = {},
            pseudonym = "Tolkien",
            onPseudonymChange = {},
            biography = "English writer and philologist.",
            onBiographyChange = {},
            email = "tolkien@example.com",
            onEmailChange = {},
            website = "https://tolkien.com",
            onWebsiteChange = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthorFormFieldsWithErrorPreview() {
    BookpublishingappTheme {
        AuthorFormFields(
            fullName = "",
            onFullNameChange = {},
            pseudonym = "",
            onPseudonymChange = {},
            biography = "",
            onBiographyChange = {},
            email = "",
            onEmailChange = {},
            website = "",
            onWebsiteChange = {},
            fullNameError = "Full name is required"
        )
    }
}
