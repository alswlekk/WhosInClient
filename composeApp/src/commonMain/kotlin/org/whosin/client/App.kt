package org.whosin.client

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.whosin.client.core.navigation.WhosInNavGraph

@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()

        WhosInNavGraph(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize(),
            navController = navController
        )
    }
}