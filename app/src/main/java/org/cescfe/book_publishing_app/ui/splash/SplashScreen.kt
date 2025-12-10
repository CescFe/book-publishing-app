package org.cescfe.book_publishing_app.ui.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.cescfe.book_publishing_app.R
import org.cescfe.book_publishing_app.ui.theme.BookpublishingappTheme

@Composable
fun SplashScreen(onSplashFinished: () -> Unit, viewModel: SplashViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    SplashScreen(
        uiState = uiState,
        onSplashFinished = onSplashFinished
    )
}

@Composable
fun SplashScreen(uiState: SplashUiState, onSplashFinished: () -> Unit) {
    val isVisible = remember { mutableStateOf(false) }
    val alphaAnim by animateFloatAsState(
        targetValue = if (isVisible.value) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "splash_alpha"
    )

    LaunchedEffect(key1 = true) {
        isVisible.value = true
    }

    LaunchedEffect(key1 = uiState.isReady) {
        if (uiState.isReady) {
            onSplashFinished()
        }
    }

    SplashContent(alpha = alphaAnim)
}

@Composable
private fun SplashContent(alpha: Float) {
    val context = LocalContext.current
    val versionName = remember {
        try {
            context.packageManager
                .getPackageInfo(context.packageName, 0)
                .versionName
        } catch (_: Exception) {
            "1.0"
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .alpha(alpha)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = R.drawable.logo_denes_blue_splash),
                contentDescription = stringResource(R.string.splash_logo_description),
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "v$versionName",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 64.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashScreenPreview() {
    BookpublishingappTheme {
        SplashContent(alpha = 1f)
    }
}
