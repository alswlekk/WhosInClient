package org.whosin.client

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.whosin.client.core.navigation.WhosInNavGraph
import ui.theme.WhosInTheme
import whosinclient.composeapp.generated.resources.Res
import whosinclient.composeapp.generated.resources.compose_multiplatform


@Composable
@Preview
fun App() {
    WhosInTheme {
        val navController = rememberNavController()

        WhosInNavGraph(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize(),
            navController = navController
        )
        // Test용으로 남겨둔 코드, 추후 삭제 예정
        // 확인하려면 위의 코드는 주석처리하고 실행
//        DummyScreen()
    }
}