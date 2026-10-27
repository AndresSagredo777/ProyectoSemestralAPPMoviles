package cl.duoc.mastermartiniapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.mastermartiniapp.model.Video
import cl.duoc.mastermartiniapp.repository.FavoritosRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** Expone qué videos son favoritos y permite marcarlos o desmarcarlos. */
class FavoritosViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = FavoritosRepository(application)

    val idsFavoritos: StateFlow<Set<Long>> = repository.favoritos
        .map { lista -> lista.map { it.id }.toSet() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptySet()
        )

    fun alternarFavorito(video: Video) {
        viewModelScope.launch { repository.alternar(video) }
    }
}
