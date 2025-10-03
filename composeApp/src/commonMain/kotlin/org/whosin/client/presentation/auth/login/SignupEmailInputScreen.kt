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
import androidx.compose.material3.CircularProgressIndicator
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
import org.whosin.client.data.repository.MemberRepository
import org.whosin.client.core.network.ApiResult
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import whosinclient.composeapp.generated.resources.Res
import whosinclient.composeapp.generated.resources.back_button
import whosinclient.composeapp.generated.resources.email_placeholder
import whosinclient.composeapp.generated.resources.next_button
import whosinclient.composeapp.generated.resources.signup_title

@Composable
fun SignupScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    onNavigateToEmailVerification: (String) -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val memberRepository: MemberRepository = koinInject()
    val coroutineScope = rememberCoroutineScope()

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
                text = stringResource(Res.string.signup_title),
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
            text = stringResource(Res.string.next_button),
            onClick = {
                if (email.isNotBlank() && !isLoading) {
                    isLoading = true

                    coroutineScope.launch {
                        when (memberRepository.sendEmailVerification(email)) {
                            is ApiResult.Success -> {
                                isLoading = false
                                onNavigateToEmailVerification(email)
                            }

                            is ApiResult.Error -> {
                                isLoading = false
                            }
                        }
                    }
                }
            },
            enabled = email.isNotBlank() && !isLoading,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp)
                .padding(bottom = 52.dp)
        )

        // 로딩 인디케이터
        if (isLoading) {
            CircularProgressIndicator(
                color = Color(0xFFF89531),
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(48.dp)
            )
        }
    }
}


@Preview
@Composable
fun EmailInputScreenPreview() {
    SignupScreen(
        modifier = Modifier,
        onNavigateBack = {},
        onNavigateToEmailVerification = { email ->
            // 이메일 처리 로직
        }
    )
}