package cl.duoc.mastermartiniapp.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import cl.duoc.mastermartiniapp.data.local.appDataStore
import cl.duoc.mastermartiniapp.model.PerfilUsuario
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 * Persistencia local del perfil con DataStore Preferences.
 * El ViewModel solo conoce este repositorio, no cómo se guardan los datos.
 */
class PerfilRepository(context: Context) {

    private val dataStore = context.applicationContext.appDataStore

    /** Flujo reactivo: emite el perfil cada vez que cambia en disco. */
    val perfil: Flow<PerfilUsuario> = dataStore.data
        .catch { error ->
            // Si el archivo no se puede leer, partimos con valores vacíos en vez de cerrar la app.
            if (error is IOException) emit(emptyPreferences()) else throw error
        }
        .map { preferencias ->
            PerfilUsuario(
                nombre = preferencias[CLAVE_NOMBRE].orEmpty(),
                telefono = preferencias[CLAVE_TELEFONO].orEmpty(),
                rutaFoto = preferencias[CLAVE_RUTA_FOTO]
            )
        }

    suspend fun guardarDatos(nombre: String, telefono: String) {
        dataStore.edit { preferencias ->
            preferencias[CLAVE_NOMBRE] = nombre
            preferencias[CLAVE_TELEFONO] = telefono
        }
    }

    suspend fun guardarRutaFoto(ruta: String?) {
        dataStore.edit { preferencias ->
            if (ruta == null) {
                preferencias.remove(CLAVE_RUTA_FOTO)
            } else {
                preferencias[CLAVE_RUTA_FOTO] = ruta
            }
        }
    }

    private companion object {
        val CLAVE_NOMBRE = stringPreferencesKey("perfil_nombre")
        val CLAVE_TELEFONO = stringPreferencesKey("perfil_telefono")
        val CLAVE_RUTA_FOTO = stringPreferencesKey("perfil_ruta_foto")
    }
}
