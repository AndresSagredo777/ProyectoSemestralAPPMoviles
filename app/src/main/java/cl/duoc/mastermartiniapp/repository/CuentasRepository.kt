package cl.duoc.mastermartiniapp.repository

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import cl.duoc.mastermartiniapp.data.local.appDataStore
import cl.duoc.mastermartiniapp.model.Cuenta
import cl.duoc.mastermartiniapp.model.Rol
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

/**
 * Persistencia local de cuentas y sesión con DataStore.
 *
 * Se guardan dos claves:
 * - "cuentas": lista de cuentas en JSON (nombre, correo, hash de contraseña, rol).
 * - "sesion_correo": correo de la cuenta que tiene la sesión abierta.
 *
 * Si nunca se ha guardado ninguna cuenta, se usan las 3 cuentas de demostración.
 */
class CuentasRepository(context: Context) {

    private val dataStore = context.applicationContext.appDataStore

    private val preferencias: Flow<Preferences> = dataStore.data
        .catch { error ->
            if (error is IOException) emit(emptyPreferences()) else throw error
        }

    /** Todas las cuentas registradas (se actualiza solo cuando cambian). */
    val cuentas: Flow<List<Cuenta>> = preferencias.map { prefs -> cuentasDe(prefs) }

    /** Cuenta con sesión abierta, o null si nadie ha iniciado sesión. */
    val sesion: Flow<Cuenta?> = preferencias.map { prefs ->
        val correo = prefs[CLAVE_SESION]
        if (correo == null) null else cuentasDe(prefs).find { it.correo == correo }
    }

    /** Devuelve la cuenta si correo y contraseña coinciden, y abre la sesión. */
    suspend fun iniciarSesion(correo: String, clave: String): Cuenta? {
        val correoNormalizado = normalizar(correo)
        val hash = HashClave.sha256(clave)
        val cuenta = cuentasDe(preferencias.first())
            .find { it.correo == correoNormalizado && it.claveHash == hash }
            ?: return null

        dataStore.edit { prefs -> prefs[CLAVE_SESION] = cuenta.correo }
        return cuenta
    }

    /**
     * Crea una cuenta nueva con rol Usuario y deja la sesión abierta.
     * Devuelve false si ya existe una cuenta con ese correo.
     */
    suspend fun registrar(nombre: String, correo: String, clave: String): Boolean {
        val correoNormalizado = normalizar(correo)
        var creada = false

        dataStore.edit { prefs ->
            val actuales = cuentasDe(prefs)
            if (actuales.none { it.correo == correoNormalizado }) {
                val nueva = Cuenta(
                    nombre = nombre.trim(),
                    correo = correoNormalizado,
                    claveHash = HashClave.sha256(clave),
                    rol = Rol.USUARIO
                )
                prefs[CLAVE_CUENTAS] = aJson(actuales + nueva)
                prefs[CLAVE_SESION] = correoNormalizado
                creada = true
            }
        }
        return creada
    }

    suspend fun cerrarSesion() {
        dataStore.edit { prefs -> prefs.remove(CLAVE_SESION) }
    }

    suspend fun cambiarRol(correo: String, nuevoRol: Rol) {
        dataStore.edit { prefs ->
            val actualizadas = cuentasDe(prefs).map { cuenta ->
                if (cuenta.correo == correo) cuenta.copy(rol = nuevoRol) else cuenta
            }
            prefs[CLAVE_CUENTAS] = aJson(actualizadas)
        }
    }

    suspend fun eliminar(correo: String) {
        dataStore.edit { prefs ->
            prefs[CLAVE_CUENTAS] = aJson(cuentasDe(prefs).filterNot { it.correo == correo })
            if (prefs[CLAVE_SESION] == correo) prefs.remove(CLAVE_SESION)
        }
    }

    // ---------- Conversión a JSON ----------

    private fun cuentasDe(prefs: Preferences): List<Cuenta> {
        val json = prefs[CLAVE_CUENTAS] ?: return CUENTAS_DEMO
        return desdeJson(json)
    }

    private fun aJson(cuentas: List<Cuenta>): String {
        val arreglo = JSONArray()
        cuentas.forEach { cuenta ->
            arreglo.put(
                JSONObject()
                    .put("nombre", cuenta.nombre)
                    .put("correo", cuenta.correo)
                    .put("claveHash", cuenta.claveHash)
                    .put("rol", cuenta.rol.name)
            )
        }
        return arreglo.toString()
    }

    private fun desdeJson(json: String): List<Cuenta> {
        return try {
            val arreglo = JSONArray(json)
            List(arreglo.length()) { indice ->
                val objeto = arreglo.getJSONObject(indice)
                Cuenta(
                    nombre = objeto.optString("nombre"),
                    correo = objeto.optString("correo"),
                    claveHash = objeto.optString("claveHash"),
                    rol = Rol.entries.find { it.name == objeto.optString("rol") } ?: Rol.USUARIO
                )
            }
        } catch (error: JSONException) {
            // Si el dato está dañado se vuelve a las cuentas de demostración.
            CUENTAS_DEMO
        }
    }

    private fun normalizar(correo: String): String = correo.trim().lowercase()

    companion object {
        private val CLAVE_CUENTAS = stringPreferencesKey("cuentas")
        private val CLAVE_SESION = stringPreferencesKey("sesion_correo")

        /** Cuentas de prueba para mostrar los 3 perfiles. */
        val CUENTAS_DEMO: List<Cuenta> = listOf(
            Cuenta("Administrador Martini", "admin@mastermartini.cl", HashClave.sha256("Admin123"), Rol.ADMIN),
            Cuenta("Chef Martini", "chef@mastermartini.cl", HashClave.sha256("Chef123"), Rol.CHEF),
            Cuenta("Usuario Demo", "usuario@mastermartini.cl", HashClave.sha256("Usuario123"), Rol.USUARIO)
        )
    }
}
