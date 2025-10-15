package org.whosin.client.presentation.auth.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.whosin.client.presentation.auth.login.viewmodel.SplashViewModel
import whosinclient.composeapp.generated.resources.Res
import whosinclient.composeapp.generated.resources.img_logo_white

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    onNavigateToLogin: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    viewModel: SplashViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        delay(1500) // 스플래시 화면 표시 시간
        viewModel.checkToken()
    }

    LaunchedEffect(uiState.isLoading) {
        if (!uiState.isLoading) {
            if (uiState.hasToken) {
                onNavigateToHome()
            } else {
                onNavigateToLogin()
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF89531)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.img_logo_white),
            contentDescription = "logo",
            modifier = Modifier.size(width = 160.dp, height = 122.dp)
        )
    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}