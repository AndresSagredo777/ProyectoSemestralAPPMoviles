package cl.duoc.mastermartiniapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.mastermartiniapp.repository.CategoriasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoriasViewModel : ViewModel() {

    private val repository = CategoriasRepository()

    private val _uiState = MutableStateFlow(
        CategoriasUiState(
            cargando = true
        )
    )

    val uiState: StateFlow<CategoriasUiState> =
        _uiState.asStateFlow()

    init {
        cargarCategorias()
    }

    fun cargarCategorias() {

        _uiState.value = CategoriasUiState(
            cargando = true
        )

        viewModelScope.launch {

            try {

                val categorias =
                    repository.obtenerCategorias()

                _uiState.value = CategoriasUiState(
                    cargando = false,
                    categorias = categorias,
                    mensajeError = null
                )

            } catch (e: Exception) {

                _uiState.value = CategoriasUiState(
                    cargando = false,
                    categorias = emptyList(),
                    mensajeError = "No se pudieron cargar las categorías."
                )
            }
        }
    }
}