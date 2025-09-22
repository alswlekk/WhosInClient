package ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable

@Composable
fun whosInTypography(): Typography {
    val fontFamily = pretendardFontFamily()

    val defaultTypography = Typography()
    return defaultTypography.copy(
        displayLarge = defaultTypography.displayLarge.copy(fontFamily = fontFamily),
        displayMedium = defaultTypography.displayMedium.copy(fontFamily = fontFamily),
        displaySmall = defaultTypography.displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = fontFamily),
        titleLarge = defaultTypography.titleLarge.copy(fontFamily = fontFamily),
        titleMedium = defaultTypography.titleMedium.copy(fontFamily = fontFamily),
        titleSmall = defaultTypography.titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = fontFamily),
        bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = defaultTypography.bodySmall.copy(fontFamily = fontFamily),
        labelLarge = defaultTypography.labelLarge.copy(fontFamily = fontFamily),
        labelMedium = defaultTypography.labelMedium.copy(fontFamily = fontFamily),
        labelSmall = defaultTypography.labelSmall.copy(fontFamily = fontFamily)
    )
}

@Composable
fun WhosInTheme(
    content: @Composable () -> Unit
) {
    val typography = whosInTypography()
    // val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        // colorScheme = colors,
        typography = typography,
        content = content
    )
}