package org.whosin.client.presentation.auth.login.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.theme.WhosInTheme

@Composable
fun CommonLoginButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = Color(0xFFF89531), // 오렌지색
    textColor: Color = Color.White
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor,
            disabledContainerColor = Color(0xFFD2D2D2),
            disabledContentColor = Color.White
        ),
        enabled = enabled
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.W600
        )
    }
}

@Preview
@Composable
fun CommonLoginButtonPreview() {
    WhosInTheme {
        CommonLoginButton(
            text = "로그인",
            onClick = {}
        )
    }
}


@Preview
@Composable
fun CommonLoginButtonDisabledPreview() {
    CommonLoginButton(
        text = "비활성화된 버튼",
        onClick = {},
        enabled = false
    )
}