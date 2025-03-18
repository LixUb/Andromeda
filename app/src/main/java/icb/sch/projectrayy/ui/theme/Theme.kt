package icb.sch.projectrayy.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Light Theme Colors
private val LightColors = lightColorScheme(
    primary = SkyBlue,
    onPrimary = White,
    primaryContainer = LightSkyBlue,
    onPrimaryContainer = DarkSkyBlue,
    secondary = SkyBlue,
    onSecondary = White,
    secondaryContainer = LightSkyBlue,
    onSecondaryContainer = DarkSkyBlue,
    tertiary = SkyBlue,
    onTertiary = White,
    tertiaryContainer = LightSkyBlue,
    onTertiaryContainer = DarkSkyBlue,
    error = Color(0xFFB00020),
    background = Color(0xFFF5F5F5),
    onBackground = Color(0xFF121212),
    surface = White,
    onSurface = Color(0xFF121212)
)

// Dark Theme Colors
private val DarkColors = darkColorScheme(
    primary = SkyBlue,
    onPrimary = White,
    primaryContainer = DarkSkyBlue,
    onPrimaryContainer = LightSkyBlue,
    secondary = LightSkyBlue,
    onSecondary = DarkSkyBlue,
    secondaryContainer = DarkSkyBlue,
    onSecondaryContainer = LightSkyBlue,
    tertiary = LightSkyBlue,
    onTertiary = DarkSkyBlue,
    tertiaryContainer = DarkSkyBlue,
    onTertiaryContainer = LightSkyBlue,
    error = Color(0xFFCF6679),
    background = Color(0xFF121212),
    onBackground = Color(0xFFEEEEEE),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFEEEEEE)
)

@Composable
fun ProjectRayyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}