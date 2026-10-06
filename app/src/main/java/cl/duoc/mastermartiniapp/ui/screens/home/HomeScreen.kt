package cl.duoc.mastermartiniapp.ui.screens.home

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import cl.duoc.mastermartiniapp.ui.utils.obtenerWindowSizeClass

@Composable
fun HomeScreen(
    onExplorarContenido: () -> Unit
) {
    val windowSizeClass = obtenerWindowSizeClass()

    when (windowSizeClass.widthSizeClass) {

        WindowWidthSizeClass.Compact -> {
            HomeScreenCompacta(
                onExplorarContenido = onExplorarContenido
            )
        }

        WindowWidthSizeClass.Medium -> {
            HomeScreenMediana(
                onExplorarContenido = onExplorarContenido
            )
        }

        WindowWidthSizeClass.Expanded -> {
            HomeScreenExpandida(
                onExplorarContenido = onExplorarContenido
            )
        }
    }
}