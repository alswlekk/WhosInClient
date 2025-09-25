package org.whosin.client.presentation.mypage.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import whosinclient.composeapp.generated.resources.Res
import whosinclient.composeapp.generated.resources.back_button

@Composable
fun MyPageTopAppBar(onNavigateBack: () -> Unit, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth().background(Color.White)) {
        AsyncImage(
            model = Res.getUri("files/ic_back.svg"),
            contentDescription = stringResource(Res.string.back_button),
            modifier = modifier.size(24.dp).clickable(onClick = onNavigateBack),
        )
        Spacer(modifier = modifier.weight(1f))
    }
}

@Preview()
@Composable
fun MyPageTopAppBarPreview() {
    MyPageTopAppBar(onNavigateBack = {})
}
