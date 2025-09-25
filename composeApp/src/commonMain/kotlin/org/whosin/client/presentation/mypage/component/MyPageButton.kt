package org.whosin.client.presentation.mypage.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MyPageButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
    text: String
) {
    Button(
        onClick = { onClick() },
        enabled = enabled,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            disabledContentColor = Color.White,
            disabledContainerColor = Color(0xFFD2D2D2),
            contentColor = Color.White,
            containerColor = Color(0xFFF89531)
        ),
        modifier = modifier.fillMaxWidth().height(54.dp).clip(RoundedCornerShape(10.dp))
    ) {
        Text(text = text, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
    }
}

@Preview
@Composable
fun MyPageButtonPreview() {
    MyPageButton(onClick = {}, text = "내 정보 수정하기", enabled = false)
}
