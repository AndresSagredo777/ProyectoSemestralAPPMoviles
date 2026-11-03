package cl.duoc.mastermartiniapp.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import cl.duoc.mastermartiniapp.data.local.appDataStore
import cl.duoc.mastermartiniapp.model.Video
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

/**
 * Guarda los videos favoritos en el dispositivo (DataStore) para verlos sin
 * volver a buscarlos. Se guardan como texto JSON usando org.json, que ya viene
 * incluido en Android, así no se agregan librerías extra.
 */
class FavoritosRepository(context: Context) {

    private val dataStore = context.applicationContext.appDataStore

    val favoritos: Flow<List<Video>> = dataStore.data
        .catch { error ->
            if (error is IOException) emit(emptyPreferences()) else throw error
        }
        .map { preferencias -> desdeJson(preferencias[CLAVE_FAVORITOS]) }

    /** Si el video ya es favorito lo quita; si no, lo agrega. */
    suspend fun alternar(video: Video) {
        dataStore.edit { preferencias ->
            val actuales = desdeJson(preferencias[CLAVE_FAVORITOS])
            val nuevos = if (actuales.any { it.id == video.id }) {
                actuales.filterNot { it.id == video.id }
            } else {
                actuales + video
            }
            preferencias[CLAVE_FAVORITOS] = aJson(nuevos)
        }
    }

    suspend fun eliminar(idVideo: Long) {
        dataStore.edit { preferencias ->
            val nuevos = desdeJson(preferencias[CLAVE_FAVORITOS]).filterNot { it.id == idVideo }
            preferencias[CLAVE_FAVORITOS] = aJson(nuevos)
        }
    }

    private fun aJson(videos: List<Video>): String {
        val arreglo = JSONArray()
        videos.forEach { video ->
            arreglo.put(
                JSONObject()
                    .put("id", video.id)
                    .put("titulo", video.titulo)
                    .put("descripcion", video.descripcion)
                    .put("categoria", video.categoria)
                    .put("urlVideo", video.urlVideo)
            )
        }
        return arreglo.toString()
    }

    private fun desdeJson(json: String?): List<Video> {
        if (json.isNullOrBlank()) return emptyList()
        return try {
            val arreglo = JSONArray(json)
            List(arreglo.length()) { indice ->
                val objeto = arreglo.getJSONObject(indice)
                Video(
                    id = objeto.getLong("id"),
                    titulo = objeto.optString("titulo"),
                    descripcion = objeto.optString("descripcion"),
                    categoria = objeto.optString("categoria"),
                    urlVideo = objeto.optString("urlVideo")
                )
            }
        } catch (error: JSONException) {
            // Un dato dañado no debe cerrar la app: se descarta y se parte de cero.
            emptyList()
        }
    }

    private companion object {
        val CLAVE_FAVORITOS = stringPreferencesKey("videos_favoritos")
    }
}
