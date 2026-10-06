package cl.duoc.mastermartiniapp.ui.screens.home

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import cl.duoc.mastermartiniapp.ui.utils.obtenerWindowSizeClass

@Composable
fun HomeScreen() {

    val windowSizeClass = obtenerWindowSizeClass()

    when (windowSizeClass.widthSizeClass) {

        WindowWidthSizeClass.Compact -> {
            HomeScreenCompacta()
        }

        WindowWidthSizeClass.Medium -> {
            HomeScreenMediana()
        }

        WindowWidthSizeClass.Expanded -> {
            HomeScreenExpandida()
        }
    }
}