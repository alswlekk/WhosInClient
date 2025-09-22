package org.whosin.client.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.whosin.client.presentation.home.mock.PresentMember
import org.whosin.client.presentation.home.mock.sampleUsers
import whosinclient.composeapp.generated.resources.Res
import whosinclient.composeapp.generated.resources.current_empty

@Composable
fun PresentMembersList(
    presentMemberList: List<PresentMember> = listOf(),
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    if (presentMemberList.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(Res.string.current_empty),
                color = Color(0xFFD2D2D2),
                fontSize = 18.sp,
                fontWeight = FontWeight(500),
            )
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                presentMemberList.forEach { member ->
                    PresentMembersItem(presentMemberNickName = member.nickname, isMe = member.isMe)
                }
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Preview
@Composable
fun PresentMembersListPreview() {
    Box(modifier = Modifier.background(Color.White).padding(16.dp)) {
        PresentMembersList(presentMemberList = sampleUsers)
    }
}