package com.kutub.smarterp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryIndigoLight,
    onPrimary = DarkBackground,
    primaryContainer = PrimaryIndigoDark,
    onPrimaryContainer = DarkTextPrimary,
    secondary = AccentViolet,
    onSecondary = Color.White,
    secondaryContainer = DarkSurfaceVariant,
    onSecondaryContainer = DarkTextPrimary,
    tertiary = IncomeGreen,
    onTertiary = Color.White,
    background = DarkBackground,
    surface = DarkSurface,
    onBackground = DarkTextPrimary,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,
    outlineVariant = DarkBorderColor,
    error = ExpenseRed,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryIndigo,
    onPrimary = Color.White,
    primaryContainer = PrimaryIndigo.copy(alpha = 0.12f),
    onPrimaryContainer = PrimaryIndigoDark,
    secondary = AccentViolet,
    onSecondary = Color.White,
    secondaryContainer = AccentViolet.copy(alpha = 0.12f),
    onSecondaryContainer = AccentViolet,
    tertiary = IncomeGreen,
    onTertiary = Color.White,
    background = LightBackground,
    surface = LightSurface,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outlineVariant = LightBorderColor,
    error = ExpenseRed,
    onError = Color.White
)

@Composable
fun SmartERPTheme(
    themeMode: String = "system",
    dynamicColor: Boolean = false,
    primaryColorHex: String? = null,
    windowWidthSizeClass: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        "dark" -> true
        "light" -> false
        else -> isSystemInDarkTheme()
    }

    var colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Apply custom primary color if provided
    primaryColorHex?.let { hex ->
        try {
            val color = Color(android.graphics.Color.parseColor(hex))
            colorScheme = colorScheme.copy(
                primary = color,
                primaryContainer = color.copy(alpha = 0.15f),
                onPrimaryContainer = if (darkTheme) Color.White else color
            )
        } catch (e: Exception) {
            // Fallback to default
        }
    }

    val dimens = when (windowWidthSizeClass) {
        WindowWidthSizeClass.Compact -> CompactDimens
        WindowWidthSizeClass.Medium -> MediumDimens
        WindowWidthSizeClass.Expanded -> ExpandedDimens
        else -> CompactDimens
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    CompositionLocalProvider(LocalDimens provides dimens) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

val MaterialTheme.dimens: Dimens
    @Composable
    get() = LocalDimens.current



