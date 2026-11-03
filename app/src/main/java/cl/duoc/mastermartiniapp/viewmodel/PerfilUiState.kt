package cl.duoc.mastermartiniapp.viewmodel

import androidx.compose.ui.graphics.ImageBitmap
import cl.duoc.mastermartiniapp.model.Video

/** Todo lo que la pantalla de perfil necesita para dibujarse. */
data class PerfilUiState(
    val cargando: Boolean = true,
    val nombre: String = "",
    val telefono: String = "",
    val nombreError: String? = null,
    val telefonoError: String? = null,
    val foto: ImageBitmap? = null,
    val procesandoFoto: Boolean = false,
    val guardando: Boolean = false,
    val guardadoExitoso: Boolean = false,
    val favoritos: List<Video> = emptyList(),
    val mensaje: String? = null
)
