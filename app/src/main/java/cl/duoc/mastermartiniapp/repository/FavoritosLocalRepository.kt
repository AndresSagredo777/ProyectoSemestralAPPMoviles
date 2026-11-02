package cl.duoc.mastermartiniapp.repository

import android.content.Context

/** Conserva localmente los identificadores de los videos favoritos. */
class FavoritosLocalRepository(context: Context) {
    private val preferences = context.getSharedPreferences("videos_favoritos", Context.MODE_PRIVATE)

    fun cargarFavoritos(): Set<Long> = preferences
        .getStringSet(KEY_FAVORITOS, emptySet())
        .orEmpty()
        .mapNotNull { it.toLongOrNull() }
        .toSet()

    fun guardarFavoritos(ids: Set<Long>) {
        preferences.edit()
            .putStringSet(KEY_FAVORITOS, ids.map(Long::toString).toSet())
            .apply()
    }

    private companion object {
        const val KEY_FAVORITOS = "ids"
    }
}

