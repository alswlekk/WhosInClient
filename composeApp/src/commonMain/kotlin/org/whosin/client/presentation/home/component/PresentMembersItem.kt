package org.whosin.client.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.jetbrains.compose.ui.tooling.preview.Preview
import whosinclient.composeapp.generated.resources.Res

@Composable
fun PresentMembersItem(
    modifier: Modifier = Modifier,
    presentMemberNickName: String = "조익성",
    isMe: Boolean = false,
) {
    val profileImages = remember {
        listOf(
            "files/ic_eyes_opened.svg",
            "files/ic_eyes_half_opened.svg",
            "files/ic_eyes_closed.svg",
            "files/ic_eyes_half_closed.svg",
        )
    }

    val imageRes = remember { profileImages.random() }
    val textColor = if (isMe) Color(0xFF7F440D) else Color(0xFF292929)

    Box(
        modifier = modifier
            .height(88.dp)
            .clip(
                shape = RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp,
                    bottomStart = 8.dp,
                    bottomEnd = 8.dp
                )
            )
            .background(Color.White)
            .border(
                width = 1.dp, color = Color(0xFFE5E5E5),
                shape = RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp,
                    bottomStart = 8.dp,
                    bottomEnd = 8.dp
                )
            )
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = Res.getUri(imageRes), // 랜덤으로 선택된 이미지를 사용
                contentDescription = "profile image",
                modifier = Modifier.height(14.dp)
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = presentMemberNickName,
                color = textColor,
                fontSize = 16.sp,
                fontWeight = FontWeight(600),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
private fun PresentMembersItemPreview() {
    PresentMembersItem()
}