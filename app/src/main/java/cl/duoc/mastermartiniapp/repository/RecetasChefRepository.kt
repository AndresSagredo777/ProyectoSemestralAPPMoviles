package cl.duoc.mastermartiniapp.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import cl.duoc.mastermartiniapp.data.local.appDataStore
import cl.duoc.mastermartiniapp.model.RecetaChef
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

/** Guarda en el dispositivo (DataStore) las recetas publicadas por los chefs. */
class RecetasChefRepository(context: Context) {

    private val dataStore = context.applicationContext.appDataStore

    val recetas: Flow<List<RecetaChef>> = dataStore.data
        .catch { error ->
            if (error is IOException) emit(emptyPreferences()) else throw error
        }
        .map { prefs -> desdeJson(prefs[CLAVE_RECETAS]) }

    suspend fun agregar(receta: RecetaChef) {
        dataStore.edit { prefs ->
            val actuales = desdeJson(prefs[CLAVE_RECETAS])
            prefs[CLAVE_RECETAS] = aJson(listOf(receta) + actuales) // la más nueva primero
        }
    }

    suspend fun eliminar(id: Long) {
        dataStore.edit { prefs ->
            prefs[CLAVE_RECETAS] = aJson(desdeJson(prefs[CLAVE_RECETAS]).filterNot { it.id == id })
        }
    }

    private fun aJson(recetas: List<RecetaChef>): String {
        val arreglo = JSONArray()
        recetas.forEach { receta ->
            arreglo.put(
                JSONObject()
                    .put("id", receta.id)
                    .put("titulo", receta.titulo)
                    .put("categoria", receta.categoria)
                    .put("ingredientes", JSONArray(receta.ingredientes))
                    .put("autorCorreo", receta.autorCorreo)
                    .put("autorNombre", receta.autorNombre)
            )
        }
        return arreglo.toString()
    }

    private fun desdeJson(json: String?): List<RecetaChef> {
        if (json.isNullOrBlank()) return emptyList()
        return try {
            val arreglo = JSONArray(json)
            List(arreglo.length()) { indice ->
                val objeto = arreglo.getJSONObject(indice)
                val ingredientes = objeto.optJSONArray("ingredientes") ?: JSONArray()
                RecetaChef(
                    id = objeto.getLong("id"),
                    titulo = objeto.optString("titulo"),
                    categoria = objeto.optString("categoria"),
                    ingredientes = List(ingredientes.length()) { i -> ingredientes.optString(i) },
                    autorCorreo = objeto.optString("autorCorreo"),
                    autorNombre = objeto.optString("autorNombre")
                )
            }
        } catch (error: JSONException) {
            emptyList()
        }
    }

    private companion object {
        val CLAVE_RECETAS = stringPreferencesKey("recetas_chef")
    }
}
