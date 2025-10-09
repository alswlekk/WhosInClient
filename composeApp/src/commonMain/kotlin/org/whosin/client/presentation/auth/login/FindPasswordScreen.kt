package org.whosin.client.presentation.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.whosin.client.presentation.auth.login.component.CommonLoginButton
import org.whosin.client.presentation.auth.login.component.CommonLoginInputField
import org.whosin.client.presentation.auth.login.viewmodel.FindPasswordUiState
import org.whosin.client.presentation.auth.login.viewmodel.FindPasswordViewModel
import whosinclient.composeapp.generated.resources.Res
import whosinclient.composeapp.generated.resources.back_button
import whosinclient.composeapp.generated.resources.email_placeholder
import whosinclient.composeapp.generated.resources.password_reset_title
import whosinclient.composeapp.generated.resources.send_email_button

@Composable
fun FindPasswordScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    onPasswordResetComplete: () -> Unit = {},
    viewModel: FindPasswordViewModel = koinViewModel()
) {
    var email by remember { mutableStateOf("") }
    var showSuccessToast by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        when (uiState) {
            is FindPasswordUiState.Success -> {
                showSuccessToast = true
                delay(2000) // 2초 후 로그인 화면으로 이동
                onPasswordResetComplete()
            }
            else -> {}
        }
    }

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
                text = stringResource(Res.string.password_reset_title),
                fontWeight = FontWeight.W600,
                fontSize = 24.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 68.dp)
            )

            CommonLoginInputField(
                value = email,
                onValueChange = { email = it },
                placeholder = stringResource(Res.string.email_placeholder)
            )
        }

        CommonLoginButton(
            text = stringResource(Res.string.send_email_button),
            onClick = {
                viewModel.sendPasswordResetEmail(email)
            },
            enabled = email.isNotBlank(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp)
                .padding(bottom = 52.dp)
        )

        // 토스트 메시지 (Snackbar)
        if (showSuccessToast) {
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 120.dp)
                    .padding(horizontal = 16.dp),
                containerColor = Color(0xFF4CAF50),
                contentColor = Color.White
            ) {
                Text(
                    text = "이메일로 임시 비밀번호가 전송되었습니다.",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500
                )
            }
        }
    }
}

@Preview
@Composable
fun PasswordResetScreenPreview() {
    FindPasswordScreen(
        modifier = Modifier,
        onNavigateBack = {},
        onPasswordResetComplete = {}
    )
}