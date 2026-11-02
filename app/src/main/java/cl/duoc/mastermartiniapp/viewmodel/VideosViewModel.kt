package cl.duoc.mastermartiniapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.mastermartiniapp.repository.FavoritosLocalRepository
import cl.duoc.mastermartiniapp.repository.VideosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VideosViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = VideosRepository()
    private val favoritosRepository = FavoritosLocalRepository(application.applicationContext)

    private val _uiState = MutableStateFlow(
        VideosUiState(favoritosIds = favoritosRepository.cargarFavoritos())
    )
    val uiState: StateFlow<VideosUiState> = _uiState.asStateFlow()

    init {
        cargarVideos()
    }

    fun cargarVideos() {
        _uiState.value = _uiState.value.copy(cargando = true, mensajeError = null)

        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    cargando = false,
                    videos = repository.obtenerVideos(),
                    mensajeError = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    cargando = false,
                    videos = emptyList(),
                    mensajeError = "No se pudieron cargar los videos."
                )
            }
        }
    }

    fun alternarFavorito(videoId: Long) {
        val favoritosActuales = _uiState.value.favoritosIds
        val nuevosFavoritos = if (videoId in favoritosActuales) {
            favoritosActuales - videoId
        } else {
            favoritosActuales + videoId
        }
        favoritosRepository.guardarFavoritos(nuevosFavoritos)
        _uiState.value = _uiState.value.copy(favoritosIds = nuevosFavoritos)
    }
}
