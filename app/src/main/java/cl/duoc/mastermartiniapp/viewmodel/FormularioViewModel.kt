package cl.duoc.mastermartiniapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import cl.duoc.mastermartiniapp.repository.FormularioLocalRepository
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

class FormularioViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FormularioLocalRepository(application.applicationContext)
    private val _uiState = MutableStateFlow(repository.cargarBorrador())
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
        val valido = nombreError == null && correoError == null && mensajeError == null
        _uiState.value = actual.copy(
            nombreError = nombreError,
            correoError = correoError,
            mensajeError = mensajeError,
            enviado = valido
        )
        if (valido) repository.guardarBorrador(actual.nombre, actual.correo, actual.mensaje)
    }

    fun limpiarConfirmacion() = actualizar { copy(enviado = false) }

    private inline fun actualizar(crossinline cambio: FormularioUiState.() -> FormularioUiState) {
        val nuevoEstado = _uiState.value.cambio()
        _uiState.value = nuevoEstado
        repository.guardarBorrador(nuevoEstado.nombre, nuevoEstado.correo, nuevoEstado.mensaje)
    }
}
