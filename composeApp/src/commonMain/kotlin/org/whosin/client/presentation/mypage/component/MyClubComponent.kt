package org.whosin.client.presentation.mypage.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.whosin.client.data.dto.response.ClubData
import ui.theme.pretendardFontFamily
import whosinclient.composeapp.generated.resources.Res

@Composable
fun MyClubComponent(
    modifier: Modifier = Modifier,
    isEditable: Boolean = false,
    myClubs: List<ClubData>,
    onDeleteClub: (Int) -> Unit,
    onNavigateToAddClub: () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "내 동아리/학과 목록",
                color = Color.Black,
                fontFamily = pretendardFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
            if (isEditable) {
                Text(
                    modifier = Modifier
                        .clickable(onClick = onNavigateToAddClub),
                    text = "추가하기",
                    color = Color(0xFFF89531),
                    fontFamily = pretendardFontFamily(),
                    fontSize = 16.sp,
                    textDecoration = TextDecoration.Underline
                )
            }
        }
        Spacer(modifier = Modifier.size(20.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color(0xFFE5E5E5),
                    shape = RoundedCornerShape(20.dp)
                ),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(myClubs) { clubData ->
                MyClubItem(
                    modifier = Modifier,
                    clubName = clubData.clubName,
                    isEditable = isEditable,
                ) {
                    println("clicked clubId : ${clubData.clubId}")
                    onDeleteClub(clubData.clubId)
                }
            }
        }
    }
}

@Composable
fun MyClubItem(
    modifier: Modifier = Modifier,
    isEditable: Boolean = false,
    clubName: String,
    onDeleteClub: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = clubName,
            color = Color.Black,
            fontFamily = pretendardFontFamily(),
            fontSize = 16.sp
        )
        if (isEditable) {
            AsyncImage(
                model = Res.getUri("files/ic_x.svg"),
                contentDescription = "Delete Club",
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = onDeleteClub)
            )
        }
    }
}

@Preview
@Composable
fun MyClubComponentPreview(

) {
    val dummyClubs = listOf(
        ClubData(clubId = 1, clubName = "메이커스팜"),
        ClubData(clubId = 2, clubName = "KUIT"),
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp)
    ) {
        MyClubComponent(
            myClubs = dummyClubs,
            onDeleteClub = {},
            onNavigateToAddClub = {}
        )
    }
}