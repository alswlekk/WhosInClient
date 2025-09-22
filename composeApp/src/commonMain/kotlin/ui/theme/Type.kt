package ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import whosinclient.composeapp.generated.resources.Res
import whosinclient.composeapp.generated.resources.bold
import whosinclient.composeapp.generated.resources.medium
import whosinclient.composeapp.generated.resources.regular
import whosinclient.composeapp.generated.resources.semibold

@Composable
fun pretendardFontFamily(): FontFamily {
    return FontFamily(
        Font(
            resource = Res.font.regular,
            weight = FontWeight.Normal,
            style = FontStyle.Normal
        ),
        Font(
            resource = Res.font.medium,
            weight = FontWeight.Medium,
            style = FontStyle.Normal
        ),
        Font(
            resource = Res.font.semibold,
            weight = FontWeight.SemiBold,
            style = FontStyle.Normal
        ),
        Font(
            resource = Res.font.bold,
            weight = FontWeight.Bold,
            style = FontStyle.Normal
        )
    )
}