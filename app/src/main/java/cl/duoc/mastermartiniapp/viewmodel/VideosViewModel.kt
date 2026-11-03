package cl.duoc.mastermartiniapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.mastermartiniapp.repository.VideosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VideosViewModel : ViewModel() {
    private val repository = VideosRepository()

    private val _uiState = MutableStateFlow(VideosUiState())
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
}
