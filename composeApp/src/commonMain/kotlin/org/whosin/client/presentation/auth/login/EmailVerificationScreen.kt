package org.whosin.client.presentation.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.whosin.client.core.network.ApiResult
import org.whosin.client.core.util.hideKeyboard
import org.whosin.client.data.repository.AuthRepository
import org.whosin.client.presentation.auth.login.component.CommonLoginButton
import org.whosin.client.presentation.auth.login.component.NumberInputBox
import whosinclient.composeapp.generated.resources.Res
import whosinclient.composeapp.generated.resources.back_button
import whosinclient.composeapp.generated.resources.confirm_button
import whosinclient.composeapp.generated.resources.email_verification_title

@Composable
fun EmailVerificationScreen(
    modifier: Modifier = Modifier,
    email: String = "",
    onNavigateBack: () -> Unit = {},
    onVerificationComplete: () -> Unit = {}
) {
    var verificationCode by remember { mutableStateOf(arrayOf("", "", "", "", "", "")) }
    var currentFocusIndex by remember { mutableStateOf(0) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val focusRequesters = remember { List(6) { FocusRequester() } }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val authRepository: AuthRepository = koinInject()
    val coroutineScope = rememberCoroutineScope()

    // 화면 진입 시 첫 번째 입력 박스에 포커스
    LaunchedEffect(Unit) {
        delay(300)
        focusRequesters[0].requestFocus()
        keyboardController?.show()
    }

    // 화면 종료 시 키보드 숨기기
    DisposableEffect(Unit) {
        onDispose {
            keyboardController?.hide()
        }
    }

    val fullCode = verificationCode.joinToString("")
    val isComplete = fullCode.length == 6

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
                text = stringResource(Res.string.email_verification_title),
                fontWeight = FontWeight.W600,
                fontSize = 24.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 68.dp)
            )

            // 6자리 인증번호
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(6) { index ->
                    NumberInputBox(
                        value = verificationCode[index],
                        onValueChange = { input ->
                            if (input.length <= 1 && input.all { it.isDigit() }) {
                                val newCode = verificationCode.copyOf()
                                newCode[index] = input
                                verificationCode = newCode

                                // 에러 메시지 초기화
                                errorMessage = null

                                // 숫자를 입력했을 때만 다음 박스로 이동
                                if (input.isNotEmpty() && index < 5) {
                                    currentFocusIndex = index + 1
                                    focusRequesters[index + 1].requestFocus()
                                }
                                // 마지막 자리 입력 완료 시 키보드 숨기기
                                else if (input.isNotEmpty() && index == 5) {
                                    focusManager.clearFocus()
                                    keyboardController?.hide()
                                    hideKeyboard()
                                }
                                // 현재 박스가 비워지고 이전 박스가 있으면 이전으로 이동
                                else if (input.isEmpty() && index > 0) {
                                    currentFocusIndex = index - 1
                                    focusRequesters[index - 1].requestFocus()
                                }
                            }
                        },
                        onBackspace = {
                            // 빈 박스에서 백스페이스 시 이전 박스로 이동
                            if (index > 0) {
                                val prevIndex = index - 1
                                currentFocusIndex = prevIndex
                                focusRequesters[prevIndex].requestFocus()
                            }
                        },
                        onFocusChanged = { isFocused ->
                            if (isFocused) {
                                currentFocusIndex = index
                            }
                        },
                        onClick = {
                            currentFocusIndex = index
                            focusRequesters[index].requestFocus()
                            keyboardController?.show()
                        },
                        isFocused = currentFocusIndex == index,
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRequesters[index])
                    )
                }
            }

            // 에러 메시지 표시
            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = Color(0xFFFF3636),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )
            }
        }

        // 하단 확인 버튼
        CommonLoginButton(
            text = stringResource(Res.string.confirm_button),
            onClick = {
                if (isComplete && !isLoading) {
                    isLoading = true

                    coroutineScope.launch {
                        when (val result = authRepository.validateEmailCode(email, fullCode)) {
                            is ApiResult.Success<*> -> {
                                isLoading = false
                                onVerificationComplete()
                            }

                            is ApiResult.Error -> {
                                isLoading = false
                                errorMessage = result.message
                            }
                        }
                    }
                }
            },
            enabled = isComplete && !isLoading,
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
fun VerificationCodeScreenPreview() {
    EmailVerificationScreen(
        modifier = Modifier,
        email = "test@example.com",
        onNavigateBack = {},
        onVerificationComplete = {
            // 인증번호 처리 로직
        }
    )
}