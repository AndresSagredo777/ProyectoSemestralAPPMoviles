package cl.duoc.mastermartiniapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.duoc.mastermartiniapp.ui.screens.categories.CategoriasScreen
import cl.duoc.mastermartiniapp.ui.screens.home.HomeScreen

@Composable
fun NavegacionApp() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Ruta.Inicio.ruta
    ) {

        composable(Ruta.Inicio.ruta) {

            HomeScreen(
                onExplorarContenido = {
                    navController.navigate(Ruta.Categorias.ruta)
                }
            )
        }

        composable(Ruta.Categorias.ruta) {
            CategoriasScreen()
        }
    }
}