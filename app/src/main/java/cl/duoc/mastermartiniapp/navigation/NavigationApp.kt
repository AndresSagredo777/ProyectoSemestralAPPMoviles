package cl.duoc.mastermartiniapp.navigation

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cl.duoc.mastermartiniapp.model.Cuenta
import cl.duoc.mastermartiniapp.ui.screens.acceso.AccesoScreen
import cl.duoc.mastermartiniapp.ui.screens.admin.AdminScreen
import cl.duoc.mastermartiniapp.ui.screens.categories.CategoriasScreen
import cl.duoc.mastermartiniapp.ui.screens.chef.ChefScreen
import cl.duoc.mastermartiniapp.ui.screens.home.HomeScreen
import cl.duoc.mastermartiniapp.ui.screens.perfil.PerfilScreen
import cl.duoc.mastermartiniapp.ui.screens.videos.VideosScreen
import cl.duoc.mastermartiniapp.ui.utils.obtenerWindowSizeClass
import cl.duoc.mastermartiniapp.viewmodel.EstadoSesion
import cl.duoc.mastermartiniapp.viewmodel.SesionViewModel

/**
 * Punto de entrada de la navegación.
 * Según la sesión guardada en DataStore decide qué mostrar:
 * - Cargando: un indicador mientras se lee el disco.
 * - Sin sesión: la pantalla de acceso.
 * - Con sesión: la app con las pestañas que corresponden al rol.
 */
@Composable
fun NavegacionApp(
    sesionViewModel: SesionViewModel = viewModel()
) {
    val estado by sesionViewModel.estado.collectAsState()

    when (val sesion = estado) {
        EstadoSesion.Cargando -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }

        EstadoSesion.SinSesion -> AccesoScreen()

        is EstadoSesion.Activa -> NavegacionPrincipal(
            cuenta = sesion.cuenta,
            onCerrarSesion = sesionViewModel::cerrarSesion
        )
    }
}

/**
 * Navegación de la app con sesión iniciada.
 * - Teléfonos (Compact/Medium): barra de navegación inferior (NavigationBar).
 * - Pantallas grandes (Expanded): riel de navegación lateral (NavigationRail).
 * Las pestañas visibles dependen del rol de la cuenta.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun NavegacionPrincipal(
    cuenta: Cuenta,
    onCerrarSesion: () -> Unit
) {
    val navController = rememberNavController()
    val entradaActual by navController.currentBackStackEntryAsState()
    val rutaActual = entradaActual?.destination?.route

    val destinos = DestinoPrincipal.entries.filter { it.visiblePara(cuenta.rol) }

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
                destinos.forEach { destino ->
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
                cuenta = cuenta,
                onCerrarSesion = onCerrarSesion,
                modifier = Modifier.weight(1f)
            )
        }
    } else {
        Scaffold(
            bottomBar = {
                NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                    destinos.forEach { destino ->
                        NavigationBarItem(
                            selected = destino.estaSeleccionado(rutaActual),
                            onClick = { irADestino(destino) },
                            icon = {
                                Icon(
                                    painter = painterResource(destino.icono),
                                    contentDescription = null
                                )
                            },
                            label = { Text(destino.etiqueta, maxLines = 1) }
                        )
                    }
                }
            }
        ) { innerPadding ->

            GrafoNavegacion(
                navController = navController,
                cuenta = cuenta,
                onCerrarSesion = onCerrarSesion,
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
    cuenta: Cuenta,
    onCerrarSesion: () -> Unit,
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

            PerfilScreen(
                cuenta = cuenta,
                onCerrarSesion = onCerrarSesion
            )
        }

        // Rutas protegidas: aunque alguien llegue a la ruta, se vuelve a revisar el rol.
        composable(Ruta.Recetas.ruta) {

            if (cuenta.rol.puedePublicarRecetas) {
                ChefScreen(cuentaActual = cuenta)
            } else {
                SinPermiso()
            }
        }

        composable(Ruta.Admin.ruta) {

            if (cuenta.rol.puedeGestionarCuentas) {
                AdminScreen(cuentaActual = cuenta)
            } else {
                SinPermiso()
            }
        }
    }
}

@Composable
private fun SinPermiso() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No tienes permiso para ver esta sección.",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
    }
}
