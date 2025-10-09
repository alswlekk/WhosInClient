package org.whosin.client

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.whosin.client.core.auth.TokenExpiredManager
import org.whosin.client.core.navigation.Route
import org.whosin.client.core.navigation.WhosInNavGraph
import ui.theme.WhosInTheme


@Composable
@Preview
fun App() {
    WhosInTheme {
        val navController = rememberNavController()
        val isTokenExpired by TokenExpiredManager.isTokenExpired.collectAsState()

        LaunchedEffect(isTokenExpired) {
            if (isTokenExpired) {
                navController.navigate(Route.Login) {
                    popUpTo(0) { inclusive = true }
                }
                TokenExpiredManager.reset()
            }
        }

        WhosInNavGraph(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize(),
            navController = navController
        )
        // Test용으로 남겨둔 코드, 추후 삭제 예정
        // 확인하려면 위의 코드는 주석처리하고 실행
//        DummyScreen()
//        TokenTestScreen()
    }
}