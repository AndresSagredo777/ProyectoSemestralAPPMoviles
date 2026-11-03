package cl.duoc.mastermartiniapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.mastermartiniapp.model.Cuenta
import cl.duoc.mastermartiniapp.model.Rol
import cl.duoc.mastermartiniapp.repository.CuentasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Lógica del panel de Administración.
 * Las reglas de negocio están aquí y no en la pantalla:
 * - un administrador no puede cambiar su propio rol ni eliminarse,
 * - siempre debe quedar al menos un administrador.
 */
class AdminViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CuentasRepository(application)

    val cuentas: StateFlow<List<Cuenta>> = repository.cuentas.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    fun cambiarRol(cuenta: Cuenta, nuevoRol: Rol, correoActual: String) {
        val error = validarCambioDeRol(cuenta, nuevoRol, correoActual, cuentas.value)
        if (error != null) {
            _mensaje.value = error
            return
        }
        if (cuenta.rol == nuevoRol) return

        viewModelScope.launch {
            repository.cambiarRol(cuenta.correo, nuevoRol)
            _mensaje.value = "${cuenta.nombre} ahora es ${nuevoRol.etiqueta}."
        }
    }

    fun eliminar(cuenta: Cuenta, correoActual: String) {
        val error = validarEliminacion(cuenta, correoActual, cuentas.value)
        if (error != null) {
            _mensaje.value = error
            return
        }
        viewModelScope.launch {
            repository.eliminar(cuenta.correo)
            _mensaje.value = "Se eliminó la cuenta de ${cuenta.nombre}."
        }
    }

    fun mensajeMostrado() {
        _mensaje.value = null
    }

    companion object {
        /** Reglas separadas en funciones puras para poder probarlas. */
        fun validarCambioDeRol(
            cuenta: Cuenta,
            nuevoRol: Rol,
            correoActual: String,
            todas: List<Cuenta>
        ): String? = when {
            cuenta.correo == correoActual -> "No puedes cambiar tu propio rol."
            cuenta.rol == Rol.ADMIN && nuevoRol != Rol.ADMIN &&
                todas.count { it.rol == Rol.ADMIN } <= 1 -> "Debe quedar al menos un administrador."
            else -> null
        }

        fun validarEliminacion(cuenta: Cuenta, correoActual: String, todas: List<Cuenta>): String? = when {
            cuenta.correo == correoActual -> "No puedes eliminar tu propia cuenta."
            cuenta.rol == Rol.ADMIN && todas.count { it.rol == Rol.ADMIN } <= 1 ->
                "Debe quedar al menos un administrador."
            else -> null
        }
    }
}
