package cl.duoc.mastermartiniapp.viewmodel

import cl.duoc.mastermartiniapp.model.Categoria

data class CategoriasUiState(
    val cargando: Boolean = false,
    val categorias: List<Categoria> = emptyList(),
    val mensajeError: String? = null
)