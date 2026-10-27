package cl.duoc.mastermartiniapp.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cl.duoc.mastermartiniapp.ui.screens.categories.CategoriasScreen
import cl.duoc.mastermartiniapp.ui.screens.home.HomeScreen
import cl.duoc.mastermartiniapp.ui.screens.videos.VideosScreen

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
                    navController.navigate(
                        Ruta.Categorias.ruta
                    )
                }
            )
        }

        composable(Ruta.Categorias.ruta) {

            CategoriasScreen(
                onCategoriaSeleccionada = { categoria ->

                    navController.navigate(
                        Ruta.Videos.crearRuta(categoria)
                    )
                }
            )
        }

        composable(
            route = Ruta.Videos.ruta,

            arguments = listOf(
                navArgument("categoria") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val categoria = Uri.decode(
                backStackEntry.arguments
                    ?.getString("categoria")
                    .orEmpty()
            )

            VideosScreen(
                categoria = categoria
            )
        }
    }
}