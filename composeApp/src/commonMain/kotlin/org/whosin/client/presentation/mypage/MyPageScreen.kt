package org.whosin.client.presentation.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.whosin.client.data.dto.response.ClubData
import org.whosin.client.presentation.component.CommonBackHandler
import org.whosin.client.presentation.mypage.component.MyClubComponent
import org.whosin.client.presentation.mypage.component.MyPageButton
import org.whosin.client.presentation.mypage.component.MyPageTopAppBar
import whosinclient.composeapp.generated.resources.Res
import whosinclient.composeapp.generated.resources.complete_edit
import whosinclient.composeapp.generated.resources.edit_my_information
import whosinclient.composeapp.generated.resources.my_information
import whosinclient.composeapp.generated.resources.nickname

@Composable
fun MyPageScreen(
    modifier: Modifier = Modifier,
    onNavigateToAddClub: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val viewModel: MyPageViewModel = koinViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    
    var showDeleteDialog by remember { mutableStateOf(false) }

    // MyPage로 돌아올 때마다 수정 모드 해제 및 데이터 새로고침
    LaunchedEffect(Unit) {
        if (uiState.isEditable) {
            viewModel.toggleEditMode()
        }
        viewModel.getMyInfo()
    }

    CommonBackHandler {
        if (uiState.isEditable) {
            viewModel.toggleEditMode()
            viewModel.getMyInfo()
        } else {
            onNavigateBack()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // MyPageButton 위 공간만 사용
                .verticalScroll(rememberScrollState())
        ) {
            MyPageTopAppBar(onNavigateBack)
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = stringResource(if (uiState.isEditable) Res.string.edit_my_information else Res.string.my_information),
                fontSize = 24.sp,
                color = Color.Black,
                lineHeight = 24.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.size(32.dp))
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(Res.string.nickname),
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                OutlinedTextField(
                    readOnly = !uiState.isEditable,
                    value = uiState.nickname,
                    onValueChange = { viewModel.updateNickName(it) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFFBFBFB),
                        unfocusedContainerColor = Color(0xFFFBFBFB),
                        focusedBorderColor = Color(0xFFE5E5E5),
                        unfocusedBorderColor = Color(0xFFE5E5E5),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    shape = RoundedCornerShape(10.dp)
                )
            }
            Spacer(modifier = Modifier.size(32.dp))
            // 내 동아리 / 학과 목록
            MyClubComponent(
                modifier = Modifier
                    .fillMaxWidth(),
                isEditable = uiState.isEditable,
                myClubs = uiState.clubs,
                onDeleteClub = { clubId ->
                    viewModel.deleteClub(clubId)
                },
                onNavigateToAddClub = onNavigateToAddClub
            )
            Spacer(modifier = Modifier.size(24.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ){
                Button(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    onClick = {
                        viewModel.logout()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF5F5F5),
                        contentColor = Color.Black
                    )
                ){
                    Text(
                        text = "로그아웃",
                        fontWeight = FontWeight.Medium
                    )
                }
                Button(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    onClick = {
                        showDeleteDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF3636),
                        contentColor = Color.White
                    )
                ){
                    Text(
                        text = "회원 탈퇴",
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
        
        // 내 정보 수정 버튼
        MyPageButton(
            onClick = {
                if (uiState.isEditable) {
                    viewModel.updateMyInfo(uiState.nickname.trim(), uiState.clubs)
                }
                viewModel.toggleEditMode()
            },
            text = stringResource(
                if (uiState.isEditable) Res.string.complete_edit
                else Res.string.edit_my_information
            ),
            enabled = uiState.nickname.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 52.dp)
        )
    }
    
    // 회원 탈퇴 확인 다이얼로그
    if (showDeleteDialog) {
        AlertDialog(
            containerColor = Color(0xFFFFFFFF),
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    text = "회원 탈퇴",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = "탈퇴 후에는 모든 데이터가 삭제되며 복구할 수 없습니다.",
                    fontSize = 16.sp
                )
            },
            confirmButton = {
                Button(
                    shape = RoundedCornerShape(10.dp),
                    onClick = {
                        showDeleteDialog = false
                        viewModel.deleteAccount()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF3636),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "확인",
                        fontWeight = FontWeight.Medium
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    shape = RoundedCornerShape(10.dp),
                    onClick = { showDeleteDialog = false },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = "취소",
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        )
    }

}

@Preview
@Composable
private fun MyPageScreenPreview() {
    MyPageScreen(
        onNavigateBack = {},
        onNavigateToAddClub = {}
    )
}

