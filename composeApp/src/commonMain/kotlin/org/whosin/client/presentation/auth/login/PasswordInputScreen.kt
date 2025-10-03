package org.whosin.client.presentation.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.whosin.client.presentation.auth.login.component.CommonLoginButton
import org.whosin.client.presentation.auth.login.component.CommonLoginInputField
import whosinclient.composeapp.generated.resources.Res
import whosinclient.composeapp.generated.resources.back_button
import whosinclient.composeapp.generated.resources.next_button
import whosinclient.composeapp.generated.resources.password_confirm_label
import whosinclient.composeapp.generated.resources.password_confirm_placeholder
import whosinclient.composeapp.generated.resources.password_input_label
import whosinclient.composeapp.generated.resources.password_input_placeholder
import whosinclient.composeapp.generated.resources.password_input_title

@Composable
fun PasswordInputScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    onPasswordComplete: (String, String) -> Unit
) {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val isComplete = password.length >= 8 &&
            confirmPassword.length >= 8 &&
            password == confirmPassword

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 30.dp),
            horizontalAlignment = Alignment.Start
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .size(24.dp)
            ) {
                AsyncImage(
                    model = Res.getUri("files/ic_back.svg"),
                    contentDescription = stringResource(Res.string.back_button),
                    modifier = Modifier.size(18.dp)
                )
            }

            Text(
                text = stringResource(Res.string.password_input_title),
                fontWeight = FontWeight.W600,
                fontSize = 24.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 68.dp)
            )

            // 비밀번호 입력 라벨
            Text(
                text = stringResource(Res.string.password_input_label),
                fontWeight = FontWeight.W500,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier
                    .padding(bottom = 10.dp)
            )

            // 비밀번호 입력 필드
            CommonLoginInputField(
                value = password,
                onValueChange = { password = it },
                placeholder = stringResource(Res.string.password_input_placeholder),
                isPassword = true,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            // 비밀번호 확인 라벨
            Text(
                text = stringResource(Res.string.password_confirm_label),
                fontWeight = FontWeight.W500,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier
                    .padding(bottom = 10.dp)
            )

            // 비밀번호 확인 입력 필드
            CommonLoginInputField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = stringResource(Res.string.password_confirm_placeholder),
                isPassword = true
            )
        }

        // 하단 다음 버튼
        CommonLoginButton(
            text = stringResource(Res.string.next_button),
            onClick = { onPasswordComplete(password, confirmPassword) },
            enabled = isComplete,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp)
                .padding(bottom = 52.dp)
        )
    }
}

@Preview
@Composable
fun PasswordInputScreenPreview() {
    PasswordInputScreen(
        modifier = Modifier,
        onNavigateBack = {},
        onPasswordComplete = { password, confirmPassword ->
            // 비밀번호 처리 로직
        }
    )
}