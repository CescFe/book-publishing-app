package org.cescfe.book_publishing_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import org.cescfe.book_publishing_app.ui.navigation.AppNavigation
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BookpublishingappTheme {
                val navController = rememberNavController()
                AppNavigation(navController = navController)
                }
            }
        }
    }
