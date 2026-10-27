package cl.duoc.mastermartiniapp.ui.screens.home

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.mastermartiniapp.ui.utils.obtenerWindowSizeClass
import cl.duoc.mastermartiniapp.viewmodel.FormularioViewModel

@Composable
fun HomeScreen(
    onExplorarContenido: () -> Unit,
    formularioViewModel: FormularioViewModel = viewModel()
) {
    val windowSizeClass = obtenerWindowSizeClass()
    val state = formularioViewModel.uiState.collectAsState().value

    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> HomeScreenCompacta(
            onExplorarContenido,
            state,
            formularioViewModel::actualizarNombre,
            formularioViewModel::actualizarCorreo,
            formularioViewModel::actualizarMensaje,
            formularioViewModel::enviar,
            formularioViewModel::limpiarConfirmacion
        )
        WindowWidthSizeClass.Medium -> HomeScreenMediana(
            onExplorarContenido,
            state,
            formularioViewModel::actualizarNombre,
            formularioViewModel::actualizarCorreo,
            formularioViewModel::actualizarMensaje,
            formularioViewModel::enviar,
            formularioViewModel::limpiarConfirmacion
        )
        WindowWidthSizeClass.Expanded -> HomeScreenExpandida(
            onExplorarContenido,
            state,
            formularioViewModel::actualizarNombre,
            formularioViewModel::actualizarCorreo,
            formularioViewModel::actualizarMensaje,
            formularioViewModel::enviar,
            formularioViewModel::limpiarConfirmacion
        )
    }
}

