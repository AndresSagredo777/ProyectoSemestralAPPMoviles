package cl.duoc.mastermartiniapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(

    primary = MartiniGreen,
    onPrimary = Color.White,

    primaryContainer = MartiniGreenLight,
    onPrimaryContainer = MartiniGreenDark,

    secondary = Gold,
    onSecondary = Charcoal,

    secondaryContainer = GoldLight,
    onSecondaryContainer = Charcoal,

    background = Cream,
    onBackground = Charcoal,

    surface = SurfaceSoft,
    onSurface = Charcoal,

    surfaceVariant = SurfaceVariantSoft,
    onSurfaceVariant = Muted,

    error = ErrorRed,
    outline = Line
)

@Composable
fun MasterMartiniApp_Grupo9Theme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}