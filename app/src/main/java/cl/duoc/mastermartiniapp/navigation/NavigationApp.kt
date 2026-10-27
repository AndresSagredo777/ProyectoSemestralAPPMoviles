package cl.duoc.mastermartiniapp.navigation

import android.net.Uri
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cl.duoc.mastermartiniapp.ui.screens.categories.CategoriasScreen
import cl.duoc.mastermartiniapp.ui.screens.home.HomeScreen
import cl.duoc.mastermartiniapp.ui.screens.perfil.PerfilScreen
import cl.duoc.mastermartiniapp.ui.screens.videos.VideosScreen
import cl.duoc.mastermartiniapp.ui.utils.obtenerWindowSizeClass

/**
 * Navegación principal de la app.
 * - Teléfonos (Compact/Medium): barra de navegación inferior (NavigationBar).
 * - Pantallas grandes (Expanded): riel de navegación lateral (NavigationRail).
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NavegacionApp() {

    val navController = rememberNavController()
    val entradaActual by navController.currentBackStackEntryAsState()
    val rutaActual = entradaActual?.destination?.route

    val usarRielLateral =
        obtenerWindowSizeClass().widthSizeClass == WindowWidthSizeClass.Expanded

    val irADestino: (DestinoPrincipal) -> Unit = { destino ->
        navController.navigate(destino.ruta.ruta) {
            // Evita acumular pantallas repetidas al cambiar de sección.
            popUpTo(navController.graph.findStartDestination().id)
            launchSingleTop = true
        }
    }

    if (usarRielLateral) {
        Row(modifier = Modifier.fillMaxSize()) {
            NavigationRail(containerColor = MaterialTheme.colorScheme.surface) {
                DestinoPrincipal.entries.forEach { destino ->
                    NavigationRailItem(
                        selected = destino.estaSeleccionado(rutaActual),
                        onClick = { irADestino(destino) },
                        icon = {
                            Icon(
                                painter = painterResource(destino.icono),
                                contentDescription = null
                            )
                        },
                        label = { Text(destino.etiqueta) }
                    )
                }
            }

            GrafoNavegacion(
                navController = navController,
                modifier = Modifier.weight(1f)
            )
        }
    } else {
        Scaffold(
            bottomBar = {
                NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                    DestinoPrincipal.entries.forEach { destino ->
                        NavigationBarItem(
                            selected = destino.estaSeleccionado(rutaActual),
                            onClick = { irADestino(destino) },
                            icon = {
                                Icon(
                                    painter = painterResource(destino.icono),
                                    contentDescription = null
                                )
                            },
                            label = { Text(destino.etiqueta) }
                        )
                    }
                }
            }
        ) { innerPadding ->

            GrafoNavegacion(
                navController = navController,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    // Las pantallas internas no vuelven a sumar el espacio de las barras del sistema.
                    .consumeWindowInsets(innerPadding)
            )
        }
    }
}

@Composable
private fun GrafoNavegacion(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Ruta.Inicio.ruta,
        modifier = modifier
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
                categoria = categoria,
                onVolver = { navController.popBackStack() }
            )
        }

        composable(Ruta.Perfil.ruta) {

            PerfilScreen()
        }
    }
}
