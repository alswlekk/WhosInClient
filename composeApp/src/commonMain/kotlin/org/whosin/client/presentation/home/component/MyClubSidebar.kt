package org.whosin.client.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.stringResource
import whosinclient.composeapp.generated.resources.Res
import whosinclient.composeapp.generated.resources.my_club_list_title

@Composable
fun MyClubSidebar(
    modifier: Modifier = Modifier,
    clubs: List<String>,
    selectedClub: String?,
    onClubSelected: (String) -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = modifier
            .background(
                color = Color.White,
                shape = RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp)
            )
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        Text(
            text = stringResource(Res.string.my_club_list_title),
            fontSize = 18.sp,
            fontWeight = FontWeight(600),
            color = Color(0xFF292929)
        )

        Spacer(modifier = Modifier.height(28.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(clubs) { clubName ->
                MyClubSidebarItem(
                    name = clubName,
                    isSelected = (clubName == selectedClub),
                    onClick = {
                        onClubSelected(clubName)
                        onClose()
                    }
                )
            }
        }
    }
}

@Composable
private fun MyClubSidebarItem(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Color(0xFFF3F3F3) else Color.Transparent

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null, // 클릭 효과 제거
                onClick = onClick
            )
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = name,
            color = Color(0xFF292929),
            fontSize = 16.sp,
            fontWeight = FontWeight(400)
        )
    }
}