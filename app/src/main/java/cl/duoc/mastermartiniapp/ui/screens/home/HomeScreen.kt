package cl.duoc.mastermartiniapp.ui.screens.home

import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.mastermartiniapp.ui.utils.obtenerWindowSizeClass
import cl.duoc.mastermartiniapp.viewmodel.FormularioViewModel

private const val PERMISO_RED_LOCAL = "android.permission.ACCESS_LOCAL_NETWORK"

@Composable
fun HomeScreen(
    onExplorarContenido: () -> Unit,
    formularioViewModel: FormularioViewModel = viewModel()
) {
    val windowSizeClass = obtenerWindowSizeClass()
    val context = LocalContext.current
    val state = formularioViewModel.uiState.collectAsState().value

    val solicitudPermiso = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            onExplorarContenido()
        } else {
            Toast.makeText(
                context,
                "Permite el acceso a la red local para cargar las categorías.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    val abrirContenido: () -> Unit = {
        val necesitaPermiso = Build.VERSION.SDK_INT >= 37 &&
            ContextCompat.checkSelfPermission(context, PERMISO_RED_LOCAL) !=
            PackageManager.PERMISSION_GRANTED

        if (necesitaPermiso) {
            solicitudPermiso.launch(PERMISO_RED_LOCAL)
        } else {
            onExplorarContenido()
        }
    }

    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> HomeScreenCompacta(
            abrirContenido,
            state,
            formularioViewModel::actualizarNombre,
            formularioViewModel::actualizarCorreo,
            formularioViewModel::actualizarMensaje,
            formularioViewModel::enviar,
            formularioViewModel::limpiarConfirmacion
        )
        WindowWidthSizeClass.Medium -> HomeScreenMediana(
            abrirContenido,
            state,
            formularioViewModel::actualizarNombre,
            formularioViewModel::actualizarCorreo,
            formularioViewModel::actualizarMensaje,
            formularioViewModel::enviar,
            formularioViewModel::limpiarConfirmacion
        )
        WindowWidthSizeClass.Expanded -> HomeScreenExpandida(
            abrirContenido,
            state,
            formularioViewModel::actualizarNombre,
            formularioViewModel::actualizarCorreo,
            formularioViewModel::actualizarMensaje,
            formularioViewModel::enviar,
            formularioViewModel::limpiarConfirmacion
        )
    }
}
