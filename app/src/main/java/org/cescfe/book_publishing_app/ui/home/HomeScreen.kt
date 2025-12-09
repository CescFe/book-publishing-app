package org.cescfe.book_publishing_app.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.cescfe.book_publishing_app.R
import org.cescfe.book_publishing_app.ui.books.components.BooksBottomBar
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme

@Composable
fun HomeScreen() {
    Scaffold(
        bottomBar = {
            BooksBottomBar()
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.home_welcome),
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    BookpublishingappTheme {
        HomeScreen()
    }
}
