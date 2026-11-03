package cl.duoc.mastermartiniapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.mastermartiniapp.model.Cuenta
import cl.duoc.mastermartiniapp.repository.CuentasRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** Los tres estados posibles de la sesión. */
sealed interface EstadoSesion {
    /** Todavía se está leyendo DataStore al abrir la app. */
    data object Cargando : EstadoSesion

    /** Nadie ha iniciado sesión: se muestra la pantalla de acceso. */
    data object SinSesion : EstadoSesion

    /** Hay una cuenta activa: se muestra la app según su rol. */
    data class Activa(val cuenta: Cuenta) : EstadoSesion
}

/**
 * Observa la sesión guardada en DataStore.
 * Como es un Flow, cuando alguien inicia o cierra sesión (o cambia su rol),
 * este estado cambia solo y la navegación se redibuja sin pasos extra.
 */
class SesionViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CuentasRepository(application)

    val estado: StateFlow<EstadoSesion> = repository.sesion
        .map { cuenta -> if (cuenta == null) EstadoSesion.SinSesion else EstadoSesion.Activa(cuenta) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = EstadoSesion.Cargando
        )

    fun cerrarSesion() {
        viewModelScope.launch { repository.cerrarSesion() }
    }
}
