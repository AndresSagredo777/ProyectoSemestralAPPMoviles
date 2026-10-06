package cl.duoc.mastermartiniapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
private val LightColorScheme = lightColorScheme(
    primary = Wine,
    onPrimary = Color.White,

    secondary = Gold,
    onSecondary = Charcoal,

    background = Cream,
    onBackground = Charcoal,

    surface = Color.White,
    onSurface = Charcoal,

    error = ErrorRed,
    outline = Line,


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