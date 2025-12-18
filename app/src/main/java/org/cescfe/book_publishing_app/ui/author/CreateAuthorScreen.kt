package org.cescfe.book_publishing_app.ui.author

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.cescfe.book_publishing_app.R
import org.cescfe.book_publishing_app.ui.shared.components.ConfirmationDialog
import org.cescfe.book_publishing_app.ui.shared.navigation.CreateBottomBar
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAuthorScreen(onNavigateUp: () -> Unit = {}) {
    var showConfirmDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.testTag("create_author_screen"),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.create_author_title)) },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateUp,
                        modifier = Modifier.testTag("back_button")
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_chevron_left),
                            contentDescription = stringResource(R.string.navigate_back)
                        )
                    }
                },
                modifier = Modifier.testTag("create_author_top_bar")
            )
        },
        bottomBar = {
            CreateBottomBar(
                onSaveClick = {
                    showConfirmDialog = true
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // TODO: Form content will be added in future tickets
        }
    }

    ConfirmationDialog(
        title = stringResource(R.string.create_author_confirmation_title),
        message = stringResource(R.string.create_author_confirmation_message),
        onDismiss = { showConfirmDialog = false },
        onConfirm = {
            showConfirmDialog = false
            // TODO: Call create author logic
        },
        isVisible = showConfirmDialog
    )
}

@Preview(showBackground = true)
@Composable
private fun CreateAuthorScreenPreview() {
    BookpublishingappTheme {
        CreateAuthorScreen()
    }
}
