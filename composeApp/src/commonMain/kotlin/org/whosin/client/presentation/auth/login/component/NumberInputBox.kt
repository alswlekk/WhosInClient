package org.whosin.client.presentation.auth.login.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun NumberInputBox(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onBackspace: (() -> Unit)? = null,
    onFocusChanged: ((Boolean) -> Unit)? = null,
    containerColor: Color = Color.White,
    borderColor: Color = Color(0xFFE5E5E5),
    focusedBorderColor: Color = Color(0xFFF89531),
    isFocused: Boolean = false
) {

    // 커서 위치 제어용
    val textFieldValue = remember(value) {
        val displayText = value.ifEmpty { "\u200B" }
        TextFieldValue(
            text = displayText,
            selection = TextRange(displayText.length)
        )
    }

    val textStyle = TextStyle(
        color = Color.Black,
        fontSize = 24.sp,
        fontWeight = FontWeight.W600,
        textAlign = TextAlign.Center
    )

    val currentBorderColor = if (isFocused) focusedBorderColor else borderColor

    Box(
        modifier = modifier
            .size(width = 50.dp, height = 54.dp)
            .background(containerColor, RoundedCornerShape(8.dp))
            .border(1.dp, currentBorderColor, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = textFieldValue,
            onValueChange = { newValue ->
                val cleaned = newValue.text.replace("\u200B", "")
                val filtered = cleaned.filter { it.isDigit() }.take(1)

                if (newValue.text.isEmpty() && value.isEmpty()) {
                    onBackspace?.invoke()
                    return@BasicTextField
                }

                onValueChange(filtered)
            },
            textStyle = textStyle.copy(
                color = if (value.isEmpty()) Color.Transparent else Color.Black
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            cursorBrush = SolidColor(Color(0xFFB2B2B2)),
            modifier = Modifier.onFocusChanged { focusState ->
                onFocusChanged?.invoke(focusState.isFocused)
            },
            decorationBox = { innerTextField ->
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    innerTextField()
                }
            }
        )
    }
}

@Preview
@Composable
fun NumberInputBoxPreview() {
    var digit by remember { mutableStateOf("") }
    NumberInputBox(
        value = digit,
        onValueChange = { digit = it }
    )
}