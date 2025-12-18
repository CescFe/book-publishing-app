package org.cescfe.book_publishing_app.ui.shared.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.cescfe.book_publishing_app.R
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme

@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    dismissText: String = stringResource(R.string.dialog_dismiss),
    confirmText: String = stringResource(R.string.dialog_confirm)
) {
    if (isVisible) {
        AlertDialog(
            modifier = modifier.testTag("confirmation_dialog"),
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            text = {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = onConfirm,
                    modifier = Modifier.testTag("confirm_button")
                ) {
                    Text(
                        text = confirmText
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("dismiss_button")
                ) {
                    Text(
                        text = dismissText
                    )
                }
            }
        )
    }
}

// ==================== PREVIEWS ====================

@Preview(showBackground = true)
@Composable
private fun ConfirmationDialogPreview() {
    BookpublishingappTheme {
        ConfirmationDialog(
            title = "Delete Author",
            message = "Are you sure you want to delete this author? This action cannot be undone.",
            onDismiss = {},
            onConfirm = {},
            isVisible = true
        )
    }
}
