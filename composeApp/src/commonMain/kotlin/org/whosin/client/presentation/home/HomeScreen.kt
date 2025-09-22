package org.whosin.client.presentation.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.whosin.client.presentation.home.component.MyClubSidebar
import org.whosin.client.presentation.home.component.PresentMembersList
import org.whosin.client.presentation.home.mock.sampleUsers
import whosinclient.composeapp.generated.resources.Res
import whosinclient.composeapp.generated.resources.current_whos_in_bottom
import whosinclient.composeapp.generated.resources.current_whos_in_top
import whosinclient.composeapp.generated.resources.ic_hamburger
import whosinclient.composeapp.generated.resources.ic_profile
import whosinclient.composeapp.generated.resources.ic_refresh
import whosinclient.composeapp.generated.resources.img_attend
import whosinclient.composeapp.generated.resources.img_home_alone
import whosinclient.composeapp.generated.resources.img_leave
import whosinclient.composeapp.generated.resources.people_count

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    onNavigateToMyPage: () -> Unit,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var isAttending by remember { mutableStateOf(true) }
    val clubs = listOf("메이커스팜", "목방", "건대교지편집위원회", "국어국문학과", "컴퓨터공학부")

    var selectedClub by remember { mutableStateOf(clubs.first()) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        scrimColor = Color(0x66000000),
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(224.dp),
                drawerShape = RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp),
                drawerContainerColor = Color.Transparent
            ) {
                MyClubSidebar(
                    modifier = Modifier.height(325.dp),
                    clubs = clubs,
                    selectedClub = selectedClub,
                    onClubSelected = { newClub ->
                        selectedClub = newClub
                    },
                    onClose = {
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(Res.drawable.img_home_alone),
                contentDescription = "Home Background",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                contentScale = ContentScale.FillWidth
            )

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(16.dp)
                ) {
                    Image(
                        painter = painterResource(Res.drawable.ic_hamburger),
                        contentDescription = "Hamburger Button",
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.CenterStart)
                            .clickable {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                    )

                    Image(
                        painter = painterResource(Res.drawable.ic_profile),
                        contentDescription = "MyPage Button",
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.CenterEnd)
                            .clickable(onClick = onNavigateToMyPage)
                    )
                }

                Column {
                    Text(
                        text = stringResource(Res.string.current_whos_in_top, selectedClub),
                        color = Color.Black,
                        fontSize = 20.sp,
                        lineHeight = 32.sp,
                        fontWeight = FontWeight(600),
                        modifier = Modifier
                            .padding(top = 4.dp, start = 20.dp)
                    )

                    Row(
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = stringResource(Res.string.current_whos_in_bottom),
                            color = Color.Black,
                            fontSize = 20.sp,
                            lineHeight = 32.sp,
                            fontWeight = FontWeight(600),
                            modifier = Modifier
                                .padding(top = 4.dp, start = 20.dp)
                        )

                        Image(
                            painter = painterResource(Res.drawable.ic_refresh),
                            contentDescription = "Refresh Button",
                            modifier = Modifier
                                .padding(start = 4.dp, bottom = 3.dp)
                                .size(24.dp)
//                        .clickable(onClick = onNavigateToMyPage)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 68.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = sampleUsers.size.toString(), // TODO: 데이터 적용
                        color = Color.Black,
                        fontSize = 45.sp,
                        lineHeight = 67.5.sp,
                        fontWeight = FontWeight(700)
                    )

                    Text(
                        text = stringResource(Res.string.people_count),
                        color = Color.Black,
                        fontSize = 20.sp,
                        lineHeight = 30.sp,
                        fontWeight = FontWeight(500),
                        modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(74.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .shadow(
                            elevation = 20.dp,
                            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                        )
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                        )
                        .padding(top = 20.dp, start = 16.dp, end = 16.dp, bottom = 118.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    PresentMembersList(presentMemberList = sampleUsers)
                }
            }

            AnimatedContent(
                targetState = isAttending,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null // 클릭 효과 제거
                    ) {
                        isAttending = !isAttending
                    },
                transitionSpec = {
                    fadeIn(animationSpec = tween(1000)) togetherWith
                            fadeOut(animationSpec = tween(1000))
                }
            ) { attending ->
                if (attending) {
                    Image(
                        painter = painterResource(Res.drawable.img_leave),
                        contentDescription = "Leave Button"
                    )
                } else {
                    Image(
                        painter = painterResource(Res.drawable.img_attend),
                        contentDescription = "Attend Button"
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        modifier = Modifier.fillMaxSize(),
        onNavigateBack = {},
        onNavigateToMyPage = {}
    )
}