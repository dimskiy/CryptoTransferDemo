package `in`.windrunner.cryptotransferdemo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun CryptoTransferDemoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme.copy(
            primary = Color.White,
            surface = Color.Black,
        )
        else -> LightColorScheme.copy(
            primary = Color.Black,
            surface = Color.White,
            onSurfaceVariant = LightGrey848
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = `in`.windrunner.cryptotransferdemo.ui.theme.Typography,
        content = content
    )
}