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
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.whosin.client.presentation.auth.login.component.CommonLoginButton
import org.whosin.client.presentation.auth.login.component.NumberInputBox
import whosinclient.composeapp.generated.resources.Res
import whosinclient.composeapp.generated.resources.back_button
import whosinclient.composeapp.generated.resources.confirm_button
import whosinclient.composeapp.generated.resources.email_verification_title

@Composable
fun EmailVerificationScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    onVerificationComplete: (String) -> Unit = {}
) {
    var verificationCode by remember { mutableStateOf(arrayOf("", "", "", "", "", "")) }
    var currentFocusIndex by remember { mutableStateOf(0) }
    val focusRequesters = remember { List(6) { FocusRequester() } }
    val keyboardController = LocalSoftwareKeyboardController.current

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
                                val wasEmpty = verificationCode[index].isEmpty()
                                newCode[index] = input
                                verificationCode = newCode

                                if (input.isNotEmpty() && index < 5) {
                                    currentFocusIndex = index + 1
                                    focusRequesters[index + 1].requestFocus()
                                } else if (input.isEmpty() && !wasEmpty && index > 0) {
                                    currentFocusIndex = index - 1
                                    focusRequesters[index - 1].requestFocus()
                                } else if (input.isNotEmpty()) {
                                    currentFocusIndex = index
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
                        isFocused = currentFocusIndex == index,
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRequesters[index])
                    )
                }
            }
        }

        // 하단 확인 버튼
        CommonLoginButton(
            text = stringResource(Res.string.confirm_button),
            onClick = { onVerificationComplete(fullCode) },
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
fun VerificationCodeScreenPreview() {
    EmailVerificationScreen(
        modifier = Modifier,
        onNavigateBack = {},
        onVerificationComplete = { code ->
            // 인증번호 처리 로직
        }
    )
}