package org.whosin.client.presentation.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
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
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.whosin.client.presentation.auth.login.component.CommonLoginButton
import org.whosin.client.presentation.auth.login.component.CommonLoginInputField
import org.whosin.client.presentation.auth.login.viewmodel.SignupUiState
import org.whosin.client.presentation.auth.login.viewmodel.SignupViewModel
import whosinclient.composeapp.generated.resources.Res
import whosinclient.composeapp.generated.resources.back_button
import whosinclient.composeapp.generated.resources.next_button
import whosinclient.composeapp.generated.resources.nickname_input_placeholder
import whosinclient.composeapp.generated.resources.nickname_input_title
import whosinclient.composeapp.generated.resources.nickname_welcome_title

@Composable
fun NicknameInputScreen(
    modifier: Modifier = Modifier,
    email: String,
    password: String,
    onNavigateBack: () -> Unit = {},
    onNavigateToClubCode: () -> Unit = {},
    viewModel: SignupViewModel = koinViewModel()
) {
    var nickname by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        when (uiState) {
            is SignupUiState.Success -> {
                onNavigateToClubCode()
            }

            is SignupUiState.Error -> {
                errorMessage = (uiState as SignupUiState.Error).message
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
                text = stringResource(Res.string.nickname_welcome_title),
                fontWeight = FontWeight.W600,
                fontSize = 24.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = stringResource(Res.string.nickname_input_title),
                fontWeight = FontWeight.W600,
                fontSize = 24.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            CommonLoginInputField(
                value = nickname,
                onValueChange = { newValue ->
                    nickname = newValue
                    errorMessage = null
                },
                placeholder = stringResource(Res.string.nickname_input_placeholder),
                maxLength = 8
            )

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage ?: "",
                    color = Color.Red,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W400
                )
            }

            if (uiState is SignupUiState.Loading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color(0xFFFF7A00)
                )
            }
        }

        // 하단 다음 버튼
        CommonLoginButton(
            text = stringResource(Res.string.next_button),
            onClick = {
                viewModel.signup(email, password, nickname)
            },
            enabled = nickname.isNotBlank() && uiState !is SignupUiState.Loading,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp)
                .padding(bottom = 52.dp)
        )
    }
}

@Preview
@Composable
fun NicknameInputScreenPreview() {
    NicknameInputScreen(
        modifier = Modifier,
        email = "test@example.com",
        password = "password123",
        onNavigateBack = {},
        onNavigateToClubCode = {}
    )
}