package cl.duoc.mastermartiniapp.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

/**
 * Única instancia de DataStore Preferences de la app.
 *
 * DataStore es el reemplazo moderno de SharedPreferences: guarda datos simples
 * de forma asíncrona y segura, y los entrega como un Flow reactivo.
 * Debe existir UNA sola instancia por archivo, por eso se declara aquí
 * y la reutilizan todos los repositorios.
 */
val Context.appDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "master_martini_preferencias"
)
