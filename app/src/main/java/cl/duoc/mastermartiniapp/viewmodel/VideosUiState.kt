package cl.duoc.mastermartiniapp.viewmodel

import cl.duoc.mastermartiniapp.model.Video

data class VideosUiState(
    val cargando: Boolean = true,
    val videos: List<Video> = emptyList(),
    val mensajeError: String? = null
)