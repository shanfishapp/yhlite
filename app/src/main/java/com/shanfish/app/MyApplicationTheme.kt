package com.shanfish.app

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// 浅紫色主题配色方案
private val LightPurpleColorScheme = lightColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF9C27B0), // 紫色
    onPrimary = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
    primaryContainer = androidx.compose.ui.graphics.Color(0xFFF3E5F5),
    onPrimaryContainer = androidx.compose.ui.graphics.Color(0xFF4A148C),
    secondary = androidx.compose.ui.graphics.Color(0xFF7B1FA2), // 深紫色
    onSecondary = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
    secondaryContainer = androidx.compose.ui.graphics.Color(0xFFE1BEE7),
    onSecondaryContainer = androidx.compose.ui.graphics.Color(0xFF4A148C),
    tertiary = androidx.compose.ui.graphics.Color(0xFFBA68C8), // 浅紫色
    onTertiary = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
    tertiaryContainer = androidx.compose.ui.graphics.Color(0xFFF3E5F5),
    onTertiaryContainer = androidx.compose.ui.graphics.Color(0xFF4A148C),
    background = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
    onBackground = androidx.compose.ui.graphics.Color(0xFF1C1B1F),
    surface = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
    onSurface = androidx.compose.ui.graphics.Color(0xFF1C1B1F),
    surfaceVariant = androidx.compose.ui.graphics.Color(0xFFE7E0EC),
    onSurfaceVariant = androidx.compose.ui.graphics.Color(0xFF49454F),
    outline = androidx.compose.ui.graphics.Color(0xFF79747E),
    inverseOnSurface = androidx.compose.ui.graphics.Color(0xFFF4EFF4),
    inverseSurface = androidx.compose.ui.graphics.Color(0xFF313033),
    error = androidx.compose.ui.graphics.Color(0xFFB3261E),
    onError = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
    errorContainer = androidx.compose.ui.graphics.Color(0xFFF9DEDC),
    onErrorContainer = androidx.compose.ui.graphics.Color(0xFF410E0B),
)

private val DarkPurpleColorScheme = darkColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFFCE93D8), // 浅紫色
    onPrimary = androidx.compose.ui.graphics.Color(0xFF4A148C),
    primaryContainer = androidx.compose.ui.graphics.Color(0xFF6A1B9A),
    onPrimaryContainer = androidx.compose.ui.graphics.Color(0xFFF3E5F5),
    secondary = androidx.compose.ui.graphics.Color(0xFFBA68C8), // 紫色
    onSecondary = androidx.compose.ui.graphics.Color(0xFF000000),
    secondaryContainer = androidx.compose.ui.graphics.Color(0xFF4A148C),
    onSecondaryContainer = androidx.compose.ui.graphics.Color(0xFFE1BEE7),
    tertiary = androidx.compose.ui.graphics.Color(0xFFD1C4E9), // 浅紫色
    onTertiary = androidx.compose.ui.graphics.Color(0xFF000000),
    tertiaryContainer = androidx.compose.ui.graphics.Color(0xFF7B1FA2),
    onTertiaryContainer = androidx.compose.ui.graphics.Color(0xFFF3E5F5),
    background = androidx.compose.ui.graphics.Color(0xFF1C1B1F),
    onBackground = androidx.compose.ui.graphics.Color(0xFFE6E1E5),
    surface = androidx.compose.ui.graphics.Color(0xFF1C1B1F),
    onSurface = androidx.compose.ui.graphics.Color(0xFFE6E1E5),
    surfaceVariant = androidx.compose.ui.graphics.Color(0xFF49454F),
    onSurfaceVariant = androidx.compose.ui.graphics.Color(0xFFCAC4D0),
    outline = androidx.compose.ui.graphics.Color(0xFF938F99),
    inverseOnSurface = androidx.compose.ui.graphics.Color(0xFF1C1B1F),
    inverseSurface = androidx.compose.ui.graphics.Color(0xFFE6E1E5),
    error = androidx.compose.ui.graphics.Color(0xFFF2B8B5),
    onError = androidx.compose.ui.graphics.Color(0xFF601410),
    errorContainer = androidx.compose.ui.graphics.Color(0xFF8C1D18),
    onErrorContainer = androidx.compose.ui.graphics.Color(0xFFF9DEDC),
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkPurpleColorScheme
        else -> LightPurpleColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = androidx.compose.material3.Typography(),
        content = content
    )
}