package cl.duoc.mastermartiniapp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class FormularioUiState(
    val nombre: String = "",
    val correo: String = "",
    val mensaje: String = "",
    val nombreError: String? = null,
    val correoError: String? = null,
    val mensajeError: String? = null,
    val enviado: Boolean = false
)

class FormularioViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(FormularioUiState())
    val uiState: StateFlow<FormularioUiState> = _uiState.asStateFlow()

    fun actualizarNombre(valor: String) = actualizar { copy(nombre = valor, nombreError = null, enviado = false) }
    fun actualizarCorreo(valor: String) = actualizar { copy(correo = valor, correoError = null, enviado = false) }
    fun actualizarMensaje(valor: String) = actualizar { copy(mensaje = valor, mensajeError = null, enviado = false) }

    fun enviar() {
        val actual = _uiState.value
        val nombreError = if (actual.nombre.isBlank()) "Escribe tu nombre." else null
        val correoError = when {
            actual.correo.isBlank() -> "Escribe tu correo electrónico."
            !android.util.Patterns.EMAIL_ADDRESS.matcher(actual.correo.trim()).matches() -> "Ingresa un correo válido."
            else -> null
        }
        val mensajeError = if (actual.mensaje.isBlank()) "Escribe un mensaje." else null
        _uiState.value = actual.copy(
            nombreError = nombreError,
            correoError = correoError,
            mensajeError = mensajeError,
            enviado = nombreError == null && correoError == null && mensajeError == null
        )
    }

    fun limpiarConfirmacion() = actualizar { copy(enviado = false) }

    private inline fun actualizar(crossinline cambio: FormularioUiState.() -> FormularioUiState) {
        _uiState.value = _uiState.value.cambio()
    }
}
