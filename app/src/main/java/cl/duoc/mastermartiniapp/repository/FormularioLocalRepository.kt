package cl.duoc.mastermartiniapp.repository

import android.content.Context
import cl.duoc.mastermartiniapp.viewmodel.FormularioUiState

/** Guarda el borrador del formulario en preferencias privadas del dispositivo. */
class FormularioLocalRepository(context: Context) {
    private val preferences = context.getSharedPreferences("formulario_contacto", Context.MODE_PRIVATE)

    fun cargarBorrador(): FormularioUiState = FormularioUiState(
        nombre = preferences.getString(KEY_NOMBRE, "").orEmpty(),
        correo = preferences.getString(KEY_CORREO, "").orEmpty(),
        mensaje = preferences.getString(KEY_MENSAJE, "").orEmpty()
    )

    fun guardarBorrador(nombre: String, correo: String, mensaje: String) {
        preferences.edit()
            .putString(KEY_NOMBRE, nombre)
            .putString(KEY_CORREO, correo)
            .putString(KEY_MENSAJE, mensaje)
            .apply()
    }

    private companion object {
        const val KEY_NOMBRE = "nombre"
        const val KEY_CORREO = "correo"
        const val KEY_MENSAJE = "mensaje"
    }
}
