package org.whosin.client.presentation.mypage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MyPageScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    onNavigateToAddClub: () -> Unit,
    onNavigateToModifyMyInfo: () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "My Page Screen"
            )
            Button(
                onClick = onNavigateToAddClub
            ){
                Text(text = "Go to Add Club")
            }
        }
    }
}

@Preview
@Composable
fun MyPageScreenPreview() {
    MyPageScreen(
        modifier = Modifier.fillMaxSize(),
        onNavigateBack = {},
        onNavigateToAddClub = {},
        onNavigateToModifyMyInfo = {}
    )
}
