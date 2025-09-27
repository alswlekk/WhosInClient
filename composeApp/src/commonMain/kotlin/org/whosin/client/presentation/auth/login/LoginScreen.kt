package org.whosin.client.presentation.auth.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import whosinclient.composeapp.generated.resources.email_label
import whosinclient.composeapp.generated.resources.email_placeholder
import whosinclient.composeapp.generated.resources.find_password_button
import whosinclient.composeapp.generated.resources.img_logo_orange
import whosinclient.composeapp.generated.resources.login_button
import whosinclient.composeapp.generated.resources.login_title
import whosinclient.composeapp.generated.resources.password_label
import whosinclient.composeapp.generated.resources.password_placeholder
import whosinclient.composeapp.generated.resources.signup_button

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onNavigateToHome: () -> Unit,
    onNavigateToFindPassword: () -> Unit = {},
    onNavigateToSignup: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 96.dp, start = 16.dp, end = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(Res.drawable.img_logo_orange),
                    contentDescription = "logo",
                    modifier = Modifier.size(width = 160.dp, height = 122.dp)
                )
                Text(
                    text = stringResource(Res.string.login_title),
                    fontWeight = FontWeight.W600,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(top = 16.dp, bottom = 32.dp)
                )


                Text(
                    text = stringResource(Res.string.email_label),
                    fontWeight = FontWeight.W500,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 10.dp)
                )
                CommonLoginInputField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = stringResource(Res.string.email_placeholder),
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                Text(
                    text = stringResource(Res.string.password_label),
                    fontWeight = FontWeight.W500,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 10.dp)
                )
                CommonLoginInputField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = stringResource(Res.string.password_placeholder),
                    isPassword = true,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                CommonLoginButton(
                    text = stringResource(Res.string.login_button),
                    onClick = onNavigateToHome,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = onNavigateToSignup,
                    modifier = Modifier.padding(horizontal = 4.dp),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = stringResource(Res.string.signup_button),
                        fontWeight = FontWeight.W500,
                        fontSize = 16.sp
                    )
                }

                TextButton(
                    onClick = onNavigateToFindPassword,
                    modifier = Modifier.padding(horizontal = 4.dp),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = stringResource(Res.string.find_password_button),
                        fontWeight = FontWeight.W500,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        modifier = Modifier,
        onNavigateToHome = {},
        onNavigateToFindPassword = {},
        onNavigateToSignup = {}
    )
}