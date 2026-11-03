package cl.duoc.mastermartiniapp.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import cl.duoc.mastermartiniapp.model.Video
import cl.duoc.mastermartiniapp.repository.FavoritosRepository
import cl.duoc.mastermartiniapp.repository.FotoPerfilRepository
import cl.duoc.mastermartiniapp.repository.PerfilRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Lógica de la pantalla de perfil (patrón MVVM):
 * - carga y guarda el perfil con DataStore (persistencia local),
 * - valida el formulario fuera del Composable,
 * - procesa la foto obtenida desde la cámara o la galería,
 * - muestra y elimina videos favoritos.
 *
 * Extiende AndroidViewModel porque los repositorios necesitan el Context.
 * SavedStateHandle guarda la Uri de la cámara por si Android cierra la app
 * mientras la cámara está abierta.
 */
class PerfilViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val perfilRepository = PerfilRepository(application)
    private val fotoRepository = FotoPerfilRepository(application)
    private val favoritosRepository = FavoritosRepository(application)

    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState: StateFlow<PerfilUiState> = _uiState.asStateFlow()

    private var trabajoConfirmacion: Job? = null

    init {
        cargarPerfil()
        observarFavoritos()
    }

    private fun cargarPerfil() {
        viewModelScope.launch {
            val perfil = perfilRepository.perfil.first()
            _uiState.update {
                it.copy(
                    cargando = false,
                    nombre = perfil.nombre,
                    telefono = perfil.telefono
                )
            }
            perfil.rutaFoto?.let { ruta ->
                _uiState.update { it.copy(procesandoFoto = true) }
                val imagen = fotoRepository.cargarImagen(ruta)
                _uiState.update { it.copy(foto = imagen, procesandoFoto = false) }
            }
        }
    }

    private fun observarFavoritos() {
        viewModelScope.launch {
            favoritosRepository.favoritos.collect { lista ->
                _uiState.update { it.copy(favoritos = lista) }
            }
        }
    }

    // ---------- Formulario ----------

    fun onNombreChange(valor: String) {
        _uiState.update { it.copy(nombre = valor, nombreError = null, guardadoExitoso = false) }
    }

    fun onTelefonoChange(valor: String) {
        // Se limita el largo para evitar textos absurdos en el campo.
        if (valor.length > 16) return
        _uiState.update { it.copy(telefono = valor, telefonoError = null, guardadoExitoso = false) }
    }

    fun guardarPerfil() {
        val actual = _uiState.value
        if (actual.guardando) return

        val nombreError = ValidadorPerfil.validarNombre(actual.nombre)
        val telefonoError = ValidadorPerfil.validarTelefono(actual.telefono)

        if (nombreError != null || telefonoError != null) {
            _uiState.update {
                it.copy(
                    nombreError = nombreError,
                    telefonoError = telefonoError,
                    mensaje = "Revisa los campos marcados en rojo."
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(guardando = true) }
            val nombreLimpio = actual.nombre.trim()
            val telefonoLimpio = ValidadorPerfil.normalizarTelefono(actual.telefono)
            perfilRepository.guardarDatos(nombreLimpio, telefonoLimpio)
            delay(400) // pausa breve para que se vea el indicador de guardado
            _uiState.update {
                it.copy(
                    guardando = false,
                    guardadoExitoso = true,
                    nombre = nombreLimpio,
                    telefono = telefonoLimpio
                )
            }
            ocultarConfirmacionLuego()
        }
    }

    private fun ocultarConfirmacionLuego() {
        trabajoConfirmacion?.cancel()
        trabajoConfirmacion = viewModelScope.launch {
            delay(3000)
            _uiState.update { it.copy(guardadoExitoso = false) }
        }
    }

    // ---------- Cámara y galería ----------

    /** Crea el archivo donde la cámara guardará la foto y recuerda su Uri. */
    fun prepararUriCamara(): Uri {
        val uri = fotoRepository.crearUriTemporalCamara()
        savedStateHandle[CLAVE_URI_CAMARA] = uri.toString()
        return uri
    }

    fun onResultadoCamara(fotoTomada: Boolean) {
        val uriTexto = savedStateHandle.get<String>(CLAVE_URI_CAMARA)
        savedStateHandle.remove<String>(CLAVE_URI_CAMARA)

        if (!fotoTomada || uriTexto == null) {
            fotoRepository.limpiarTemporales()
            mostrarMensaje("Captura cancelada.")
            return
        }
        procesarImagen(Uri.parse(uriTexto), origen = "la cámara")
    }

    fun onImagenGaleria(uri: Uri?) {
        if (uri == null) {
            mostrarMensaje("No seleccionaste ninguna imagen.")
            return
        }
        procesarImagen(uri, origen = "la galería")
    }

    fun onCamaraNoDisponible() {
        savedStateHandle.remove<String>(CLAVE_URI_CAMARA)
        mostrarMensaje("No se encontró una aplicación de cámara en este dispositivo.")
    }

    fun eliminarFoto() {
        viewModelScope.launch {
            fotoRepository.eliminarFotoPerfil()
            perfilRepository.guardarRutaFoto(null)
            _uiState.update { it.copy(foto = null) }
            mostrarMensaje("Foto de perfil eliminada.")
        }
    }

    private fun procesarImagen(uri: Uri, origen: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(procesandoFoto = true) }
            try {
                val ruta = fotoRepository.guardarComoFotoPerfil(uri)
                perfilRepository.guardarRutaFoto(ruta)
                val imagen = fotoRepository.cargarImagen(ruta)
                _uiState.update { it.copy(foto = imagen, procesandoFoto = false) }
                mostrarMensaje("Foto actualizada desde $origen.")
            } catch (error: Exception) {
                _uiState.update { it.copy(procesandoFoto = false) }
                mostrarMensaje("No se pudo usar esa imagen. Intenta con otra.")
            }
        }
    }

    // ---------- Favoritos ----------

    fun quitarFavorito(video: Video) {
        viewModelScope.launch {
            favoritosRepository.eliminar(video.id)
            mostrarMensaje("\"${video.titulo}\" se quitó de favoritos.")
        }
    }

    // ---------- Mensajes ----------

    private fun mostrarMensaje(texto: String) {
        _uiState.update { it.copy(mensaje = texto) }
    }

    /** La pantalla avisa que ya mostró el mensaje (Snackbar). */
    fun mensajeMostrado() {
        _uiState.update { it.copy(mensaje = null) }
    }

    private companion object {
        const val CLAVE_URI_CAMARA = "uri_camara_pendiente"
    }
}
