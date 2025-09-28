package org.whosin.client.presentation.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.whosin.client.data.dto.response.ClubData
import org.whosin.client.presentation.mypage.component.MyClubComponent
import org.whosin.client.presentation.mypage.component.MyPageButton
import org.whosin.client.presentation.mypage.component.MyPageTopAppBar
import whosinclient.composeapp.generated.resources.Res
import whosinclient.composeapp.generated.resources.edit_my_information
import whosinclient.composeapp.generated.resources.my_information
import whosinclient.composeapp.generated.resources.nickname

@Composable
fun MyPageScreen(
    modifier: Modifier = Modifier,
    onNavigateToAddClub: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: () -> Unit
) {
    val viewModel: MyPageViewModel = koinViewModel()
    var nickName by remember { mutableStateOf("조익성") }
    val myClubs = listOf(
        ClubData(clubId = 1, clubName = "메이커스팜"),
        ClubData(clubId = 2, clubName = "KUIT"),
        ClubData(clubId = 1, clubName = "메이커스팜"),
        ClubData(clubId = 2, clubName = "KUIT"),
        ClubData(clubId = 1, clubName = "메이커스팜"),
        ClubData(clubId = 2, clubName = "KUIT"),
        ClubData(clubId = 1, clubName = "메이커스팜"),
        ClubData(clubId = 2, clubName = "KUIT"),
        ClubData(clubId = 1, clubName = "메이커스팜"),
        ClubData(clubId = 2, clubName = "KUIT"),
        ClubData(clubId = 1, clubName = "메이커스팜"),
        ClubData(clubId = 2, clubName = "KUIT"),
    )
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            MyPageTopAppBar(onNavigateBack)
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = stringResource(Res.string.my_information),
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
                    readOnly = true,
                    value = nickName,
                    onValueChange = { nickName = it },
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
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 72.dp),
                myClubs = myClubs,
                onDeleteClub = {
                    // TODO: 동아리 삭제
//                    viewModel.deleteClub(it)
                },
                onNavigateToAddClub = { onNavigateToAddClub }
            )
        }
        
        // 내 정보 수정 버튼 - 하단에 고정
        MyPageButton(
            onClick = onNavigateToEdit,
            text = stringResource(Res.string.edit_my_information),
            enabled = nickName.isNotEmpty(),
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

}

@Preview
@Composable
private fun MyPageScreenPreview() {
    MyPageScreen(
        onNavigateBack = {},
        onNavigateToEdit = {},
        onNavigateToAddClub = {}
    )
}

