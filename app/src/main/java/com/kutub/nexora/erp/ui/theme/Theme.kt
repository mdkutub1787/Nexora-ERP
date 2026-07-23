package com.kutub.nexora.erp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
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
    onSecondary = androidx.compose.ui.graphics.Color.White,
    secondaryContainer = DarkSurfaceVariant,
    onSecondaryContainer = DarkTextPrimary,
    tertiary = IncomeGreen,
    onTertiary = androidx.compose.ui.graphics.Color.White,
    background = DarkBackground,
    surface = DarkSurface,
    onBackground = DarkTextPrimary,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,
    outlineVariant = DarkBorderColor,
    error = ExpenseRed,
    onError = androidx.compose.ui.graphics.Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryIndigo,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = PrimaryIndigo.copy(alpha = 0.12f),
    onPrimaryContainer = PrimaryIndigoDark,
    secondary = AccentViolet,
    onSecondary = androidx.compose.ui.graphics.Color.White,
    secondaryContainer = AccentViolet.copy(alpha = 0.12f),
    onSecondaryContainer = AccentViolet,
    tertiary = IncomeGreen,
    onTertiary = androidx.compose.ui.graphics.Color.White,
    background = LightBackground,
    surface = LightSurface,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outlineVariant = LightBorderColor,
    error = ExpenseRed,
    onError = androidx.compose.ui.graphics.Color.White
)

@Composable
fun NexoraERPTheme(
    themeMode: String = "system",
    dynamicColor: Boolean = false,
    windowWidthSizeClass: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        "dark" -> true
        "light" -> false
        else -> isSystemInDarkTheme()
    }

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
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
            window.statusBarColor = colorScheme.surface.toArgb()
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
