package cl.duoc.mastermartiniapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.mastermartiniapp.model.Categoria
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoriasViewModel : ViewModel() {

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

                // Simula una carga mientras todavía no tenemos API.
                delay(800)

                val categorias = obtenerCategorias()

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

    private fun obtenerCategorias(): List<Categoria> {

        return listOf(
            Categoria(
                nombre = "Chocolatería",
                descripcion = "Técnicas, recetas y preparaciones con chocolate"
            ),
            Categoria(
                nombre = "Pastelería",
                descripcion = "Postres y técnicas profesionales"
            ),
            Categoria(
                nombre = "Panadería",
                descripcion = "Masas, panes y procesos de horneado"
            ),
            Categoria(
                nombre = "Heladería",
                descripcion = "Preparaciones frías y técnicas de heladería"
            ),
            Categoria(
                nombre = "Bollería",
                descripcion = "Masas laminadas y productos de bollería"
            ),
            Categoria(
                nombre = "Galletas y horneo",
                descripcion = "Productos horneados y técnicas de cocción"
            )
        )
    }
}