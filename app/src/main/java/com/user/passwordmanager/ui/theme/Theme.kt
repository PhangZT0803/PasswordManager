package com.user.passwordmanager.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = SecureBlue,
    onPrimary = Color.White,
    primaryContainer = SecureBlueContainer,
    onPrimaryContainer = Color.White,
    background = Color(0xFF121212),
    surface = SurfaceSubtle,
    onSurface = OnSurfaceDark,
    onSurfaceVariant = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    outline = OutlineDark,
    error = Color(0xFFEA4335),
    onBackground = Color(0xFFE8EAED)
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun PasswordManagerTheme(
    themeIndex: Int = 0,          // 从外面传进来
    content: @Composable () -> Unit  // 补上 content 参数
) {
    val darkTheme = when (themeIndex) {
        1 -> false
        2 -> true
        else -> isSystemInDarkTheme()
    }

    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme  // 赋值

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}