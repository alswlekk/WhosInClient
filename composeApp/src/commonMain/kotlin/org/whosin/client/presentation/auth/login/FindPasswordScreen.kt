package org.whosin.client.presentation.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.whosin.client.presentation.auth.login.component.CommonLoginButton
import org.whosin.client.presentation.auth.login.component.CommonLoginInputField
import whosinclient.composeapp.generated.resources.Res
import whosinclient.composeapp.generated.resources.back_button
import whosinclient.composeapp.generated.resources.email_placeholder
import whosinclient.composeapp.generated.resources.ic_back
import whosinclient.composeapp.generated.resources.password_reset_title
import whosinclient.composeapp.generated.resources.send_email_button

@Composable
fun FindPasswordScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    onPasswordResetComplete: (String) -> Unit = {}
) {
    var email by remember { mutableStateOf("") }

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
                Icon(
                    painter = painterResource(Res.drawable.ic_back),
                    contentDescription = stringResource(Res.string.back_button),
                    tint = Color.Black,
                    modifier = Modifier
                        .size(18.dp)
                )
            }

            Text(
                text = stringResource(Res.string.password_reset_title),
                fontWeight = FontWeight.W600,
                fontSize = 24.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 68.dp)
            )

            CommonLoginInputField(
                value = email,
                onValueChange = { email = it },
                placeholder = stringResource(Res.string.email_placeholder)
            )
        }

        CommonLoginButton(
            text = stringResource(Res.string.send_email_button),
            onClick = { onPasswordResetComplete(email) },
            enabled = email.isNotBlank(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp)
                .padding(bottom = 36.dp)
        )
    }
}

@Preview
@Composable
fun PasswordResetScreenPreview() {
    FindPasswordScreen(
        modifier = Modifier,
        onNavigateBack = {},
        onPasswordResetComplete = { email ->
            // 이메일 처리 로직
        }
    )
}