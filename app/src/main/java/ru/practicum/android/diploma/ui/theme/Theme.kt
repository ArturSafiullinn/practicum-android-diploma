package ru.practicum.android.diploma.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    // Акцент
    primary = Blue,
    onPrimary = White,

    // Фон экранов
    background = White,
    onBackground = Black,

    // Поверхности (карточки/панели/BottomBar)
    // BottomNavigation -> Gray400
    surface = White,
    onSurface = Black,

    // Серые контейнеры (TextField фон и т.п.)
    surfaceVariant = Gray100,
    onSurfaceVariant = Black,

    // цвет комментариев в окнах( поиск, выбор региона, ожидаемая зп)
    inverseSurface = Gray400
)

private val DarkColorScheme = darkColorScheme(
    // Акцент
    primary = Blue,
    onPrimary = White,

    // Фон экранов
    background = Black,
    onBackground = White,

    // Поверхности (карточки/панели/BottomBar) в тёмной теме -> Gray400
    surface = Black,
    onSurface = White,

    // Серые контейнеры (TextField фон и т.п.) в тёмной теме -> Gray400
    surfaceVariant = Gray400,
    onSurfaceVariant = Black,

    // цвет комментариев в окнах( поиск, выбор региона, ожидаемая зп)
    inverseSurface = White
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
