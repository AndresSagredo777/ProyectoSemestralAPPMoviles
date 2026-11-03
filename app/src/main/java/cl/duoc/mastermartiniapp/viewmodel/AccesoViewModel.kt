package cl.duoc.mastermartiniapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.mastermartiniapp.repository.CuentasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Estado de la pantalla de acceso (iniciar sesión / crear cuenta). */
data class AccesoUiState(
    val modoRegistro: Boolean = false,
    val nombre: String = "",
    val correo: String = "",
    val clave: String = "",
    val confirmacion: String = "",
    val mostrarClave: Boolean = false,
    val nombreError: String? = null,
    val correoError: String? = null,
    val claveError: String? = null,
    val confirmacionError: String? = null,
    val errorGeneral: String? = null,
    val cargando: Boolean = false
)

/**
 * Lógica de acceso. Valida con ValidadorAcceso y usa CuentasRepository.
 * No navega: al guardar la sesión en DataStore, SesionViewModel lo detecta
 * y la app cambia sola a la pantalla principal.
 */
class AccesoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CuentasRepository(application)

    private val _uiState = MutableStateFlow(AccesoUiState())
    val uiState: StateFlow<AccesoUiState> = _uiState.asStateFlow()

    fun onNombreChange(valor: String) =
        _uiState.update { it.copy(nombre = valor, nombreError = null, errorGeneral = null) }

    fun onCorreoChange(valor: String) =
        _uiState.update { it.copy(correo = valor, correoError = null, errorGeneral = null) }

    fun onClaveChange(valor: String) =
        _uiState.update { it.copy(clave = valor, claveError = null, errorGeneral = null) }

    fun onConfirmacionChange(valor: String) =
        _uiState.update { it.copy(confirmacion = valor, confirmacionError = null, errorGeneral = null) }

    fun alternarMostrarClave() =
        _uiState.update { it.copy(mostrarClave = !it.mostrarClave) }

    /** Cambia entre "Iniciar sesión" y "Crear cuenta" limpiando errores y contraseñas. */
    fun alternarModo() = _uiState.update {
        AccesoUiState(modoRegistro = !it.modoRegistro, correo = it.correo)
    }

    /** Rellena el formulario con una cuenta de prueba (útil en la presentación). */
    fun usarCuentaDemo(correo: String, clave: String) =
        _uiState.update { AccesoUiState(correo = correo, clave = clave) }

    fun enviar() {
        val actual = _uiState.value
        if (actual.cargando) return

        val nombreError = if (actual.modoRegistro) ValidadorAcceso.validarNombre(actual.nombre) else null
        val correoError = ValidadorAcceso.validarCorreo(actual.correo)
        val claveError = if (actual.modoRegistro) {
            ValidadorAcceso.validarClaveNueva(actual.clave)
        } else {
            ValidadorAcceso.validarClaveIngreso(actual.clave)
        }
        val confirmacionError = if (actual.modoRegistro) {
            ValidadorAcceso.validarConfirmacion(actual.clave, actual.confirmacion)
        } else {
            null
        }

        if (listOfNotNull(nombreError, correoError, claveError, confirmacionError).isNotEmpty()) {
            _uiState.update {
                it.copy(
                    nombreError = nombreError,
                    correoError = correoError,
                    claveError = claveError,
                    confirmacionError = confirmacionError
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true, errorGeneral = null) }

            if (actual.modoRegistro) {
                val creada = repository.registrar(actual.nombre, actual.correo, actual.clave)
                if (creada) {
                    _uiState.value = AccesoUiState() // se limpian los datos, incluida la contraseña
                } else {
                    _uiState.update {
                        it.copy(cargando = false, correoError = "Ya existe una cuenta con este correo.")
                    }
                }
            } else {
                val cuenta = repository.iniciarSesion(actual.correo, actual.clave)
                if (cuenta != null) {
                    _uiState.value = AccesoUiState()
                } else {
                    _uiState.update {
                        it.copy(
                            cargando = false,
                            clave = "",
                            errorGeneral = "Correo o contraseña incorrectos."
                        )
                    }
                }
            }
        }
    }
}
