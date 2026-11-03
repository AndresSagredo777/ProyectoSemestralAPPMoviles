package cl.duoc.mastermartiniapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.mastermartiniapp.model.Cuenta
import cl.duoc.mastermartiniapp.model.RecetaChef
import cl.duoc.mastermartiniapp.repository.RecetasChefRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Estado del formulario "Publicar receta" y de la lista de recetas. */
data class ChefUiState(
    val titulo: String = "",
    val categoria: String = "",
    val ingredientesTexto: String = "",
    val tituloError: String? = null,
    val categoriaError: String? = null,
    val ingredientesError: String? = null,
    val publicada: Boolean = false,
    val recetas: List<RecetaChef> = emptyList(),
    val mensaje: String? = null
)

/** Lógica del panel del Chef: valida y guarda recetas con DataStore. */
class ChefViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RecetasChefRepository(application)

    private val _uiState = MutableStateFlow(ChefUiState())
    val uiState: StateFlow<ChefUiState> = _uiState.asStateFlow()

    private var trabajoConfirmacion: Job? = null

    init {
        viewModelScope.launch {
            repository.recetas.collect { lista -> _uiState.update { it.copy(recetas = lista) } }
        }
    }

    fun onTituloChange(valor: String) =
        _uiState.update { it.copy(titulo = valor, tituloError = null, publicada = false) }

    fun onCategoriaChange(valor: String) =
        _uiState.update { it.copy(categoria = valor, categoriaError = null, publicada = false) }

    fun onIngredientesChange(valor: String) =
        _uiState.update { it.copy(ingredientesTexto = valor, ingredientesError = null, publicada = false) }

    fun publicar(autor: Cuenta) {
        val actual = _uiState.value
        val tituloError = ValidadorReceta.validarTitulo(actual.titulo)
        val categoriaError = ValidadorReceta.validarCategoria(actual.categoria)
        val ingredientesError = ValidadorReceta.validarIngredientes(actual.ingredientesTexto)

        if (tituloError != null || categoriaError != null || ingredientesError != null) {
            _uiState.update {
                it.copy(
                    tituloError = tituloError,
                    categoriaError = categoriaError,
                    ingredientesError = ingredientesError
                )
            }
            return
        }

        viewModelScope.launch {
            repository.agregar(
                RecetaChef(
                    id = System.currentTimeMillis(),
                    titulo = actual.titulo.trim(),
                    categoria = actual.categoria,
                    ingredientes = ValidadorReceta.separarIngredientes(actual.ingredientesTexto),
                    autorCorreo = autor.correo,
                    autorNombre = autor.nombre
                )
            )
            // Se limpia el formulario y se muestra la confirmación animada.
            _uiState.update {
                it.copy(titulo = "", categoria = "", ingredientesTexto = "", publicada = true)
            }
            trabajoConfirmacion?.cancel()
            trabajoConfirmacion = launch {
                delay(3000)
                _uiState.update { it.copy(publicada = false) }
            }
        }
    }

    /** El autor puede borrar sus recetas; el Administrador puede borrar cualquiera. */
    fun eliminar(receta: RecetaChef, solicitante: Cuenta) {
        if (receta.autorCorreo != solicitante.correo && !solicitante.rol.puedeGestionarCuentas) {
            _uiState.update { it.copy(mensaje = "Solo puedes eliminar tus propias recetas.") }
            return
        }
        viewModelScope.launch {
            repository.eliminar(receta.id)
            _uiState.update { it.copy(mensaje = "Receta \"${receta.titulo}\" eliminada.") }
        }
    }

    fun mensajeMostrado() = _uiState.update { it.copy(mensaje = null) }
}
