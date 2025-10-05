package org.whosin.client.presentation.auth.clubcode

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.whosin.client.presentation.auth.login.component.CommonLoginButton
import org.whosin.client.presentation.auth.login.component.NumberInputBox
import whosinclient.composeapp.generated.resources.Res
import whosinclient.composeapp.generated.resources.back_button
import whosinclient.composeapp.generated.resources.club_code_confirm_button
import whosinclient.composeapp.generated.resources.club_code_error_message
import whosinclient.composeapp.generated.resources.club_code_title_1
import whosinclient.composeapp.generated.resources.club_code_title_2
import whosinclient.composeapp.generated.resources.confirm_button


@Composable
fun ClubCodeInputScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    viewModel: AddClubViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    var clubCode by remember { mutableStateOf(arrayOf("", "", "", "", "", "")) }
    var currentFocusIndex by remember { mutableStateOf(0) }
    val currentState = uiState.verificationState
    val focusRequesters = remember { List(6) { FocusRequester() } }
    val keyboardController = LocalSoftwareKeyboardController.current

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

    LaunchedEffect(currentState) {
        if (currentState == ClubCodeState.ERROR) {
            delay(1000)
            clubCode = arrayOf("", "", "", "", "", "")
            currentFocusIndex = 0
            focusRequesters[0].requestFocus()
            viewModel.resetErrorState()
        }
        if (currentState == ClubCodeState.SUCCESS){
            keyboardController?.hide()
        }
    }

    // 동아리 추가 성공 시 홈으로 이동
    LaunchedEffect(uiState.isAddClubSuccess) {
        if (uiState.isAddClubSuccess) {
            onNavigateToHome()
        }
    }

    val fullCode = clubCode.joinToString("")
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

            // 제목
            Text(
                text = stringResource(Res.string.club_code_title_1),
                fontWeight = FontWeight.W600,
                fontSize = 24.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = stringResource(Res.string.club_code_title_2),
                fontWeight = FontWeight.W600,
                fontSize = 24.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 68.dp)
            )

            // 6자리 번호 입력
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(6) { index ->
                    NumberInputBox(
                        value = clubCode[index],
                        onValueChange = { input ->
                            if (input.length <= 1 && input.all { it.isDigit() }) {
                                val newCode = clubCode.copyOf()
                                newCode[index] = input
                                clubCode = newCode

                                // 숫자를 입력했을 때만 다음 박스로 이동
                                if (input.isNotEmpty() && index < 5) {
                                    currentFocusIndex = index + 1
                                    focusRequesters[index + 1].requestFocus()
                                    keyboardController?.show()
                                }
                                // 현재 박스가 비워지고 이전 박스가 있으면 이전으로 이동
                                else if (input.isEmpty() && index > 0) {
                                    currentFocusIndex = index - 1
                                    focusRequesters[index - 1].requestFocus()
                                    keyboardController?.show()
                                }
                            }
                        },
                        onBackspace = {
                            if (index > 0) {
                                val prevIndex = index - 1
                                currentFocusIndex = prevIndex
                                focusRequesters[prevIndex].requestFocus()
                                keyboardController?.show()
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
                        borderColor = when (currentState) {
                            ClubCodeState.ERROR -> Color(0xFFFF3636)
                            else -> Color(0xFFE5E5E5)
                        },
                        focusedBorderColor = when (currentState) {
                            ClubCodeState.ERROR -> Color(0xFFFF3636)
                            else -> Color(0xFFF89531)
                        },
                        isFocused = currentFocusIndex == index,
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRequesters[index])
                    )
                }
            }

            // 에러 메시지
            if (currentState == ClubCodeState.ERROR) {
                Text(
                    text = uiState.errorMessage?:"예상치 못한 오류가 발생했습니다",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500,
                    color = Color(0xFFFF3636),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 12.dp)
                        .fillMaxWidth()
                )
            }


            Box(
                modifier = Modifier
                    .padding(top = if (currentState != ClubCodeState.ERROR) 48.dp else 0.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CommonLoginButton(
                    text = stringResource(Res.string.club_code_confirm_button),
                    onClick = {
                        if (isComplete) {
                            viewModel.confirmClubCode(clubCode = fullCode)
                            if (currentState == ClubCodeState.SUCCESS){
                                keyboardController?.hide()
                            }
                        }
                    },
                    enabled = isComplete,
                    modifier = Modifier
                        .size(width = 108.dp, height = 44.dp)
                )
            }


            if (currentState == ClubCodeState.SUCCESS) {
                // 동아리 정보 박스
                Box(
                    modifier = Modifier
                        .padding(top = 56.dp)
                        .padding(horizontal = 40.dp)
                        .fillMaxWidth()
                        .height(88.dp)
                        .background(
                            color = Color(0xFFFBFBFB),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = Color(0xFFE5E5E5),
                            shape = RoundedCornerShape(8.dp)
                        ),

                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.clubName ?: "",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                }

                // 동아리 추가 실패 에러 메시지
                if (uiState.errorMessage != null && !uiState.isAddClubSuccess) {
                    Text(
                        text = uiState.errorMessage,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500,
                        color = Color(0xFFFF3636),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }

        // 하단 확인 버튼
        CommonLoginButton(
            text = stringResource(Res.string.confirm_button),
            onClick = {
                if (currentState == ClubCodeState.SUCCESS) {
                    if (uiState.clubId != null){
                        viewModel.addClub(uiState.clubId)
                    }
                } else {
                    println("ClubCodeInputScreen : 확인 버튼 오류")
                }
            },
            enabled = currentState == ClubCodeState.SUCCESS,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp)
                .padding(bottom = 52.dp)
        )
    }
}

@Preview
@Composable
fun ClubCodeInputScreenPreview() {
    ClubCodeInputScreen(
        modifier = Modifier,
        onNavigateBack = {},
        onNavigateToHome = { }
    )
}