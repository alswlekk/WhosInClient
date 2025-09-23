package org.whosin.client.presentation.auth.login.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CommonLoginInputField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false,
    maxLength: Int? = null
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val isMaxLengthReached = maxLength != null && value.length >= maxLength
    OutlinedTextField(
        value = value,
        onValueChange = { if (maxLength == null || it.length <= maxLength) onValueChange(it) },
        placeholder = {
            Text(
                text = placeholder,
                color = Color(0xFFB2B2B2),
                fontSize = 16.sp,
                fontWeight = FontWeight.W500
            )
        },
        textStyle = TextStyle(
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.W500
        ),
        modifier = modifier
            .height(54.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = if (isMaxLengthReached) Color(0xFFE5E5E5) else Color(0xFFF89531),
            unfocusedBorderColor = Color(0xFFE5E5E5),
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            cursorColor = Color(0xFFB2B2B2)
        ),
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = if (isPassword && value.isNotEmpty()) {
            {
                IconButton(
                    onClick = { passwordVisible = !passwordVisible }
                ) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (passwordVisible) "비밀번호 숨기기" else "비밀번호 보기",
                        tint = Color(0xFFB2B2B2),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        } else null,
        singleLine = true
    )
}

@Preview()
@Composable
fun CommonLoginInputFieldPreview() {
    var text by remember { mutableStateOf("") }
    CommonLoginInputField(
        value = text,
        onValueChange = { text = it },
        placeholder = "이메일을 입력해주세요"
    )
}

@Preview
@Composable
fun CommonLoginInputFieldPasswordPreview() {
    var password by remember { mutableStateOf("password123") }
    CommonLoginInputField(
        value = password,
        onValueChange = { password = it },
        placeholder = "비밀번호를 입력해주세요",
        isPassword = true
    )
}
