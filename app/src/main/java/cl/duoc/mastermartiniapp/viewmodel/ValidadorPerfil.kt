package cl.duoc.mastermartiniapp.viewmodel

/**
 * Reglas de validación del formulario de perfil.
 * Están separadas de la interfaz (no dependen de Compose) para que el
 * ViewModel las use y se puedan probar con pruebas unitarias.
 * Cada función devuelve el mensaje de error, o null si el dato es válido.
 */
object ValidadorPerfil {

    private val SOLO_LETRAS = Regex("^[A-Za-zÁÉÍÓÚáéíóúÑñÜü ]+$")

    // Celular chileno: 9 + 8 dígitos, con o sin +56 al inicio.
    private val CELULAR_CHILE = Regex("^(\\+?56)?9\\d{8}$")

    fun validarNombre(nombre: String): String? {
        val limpio = nombre.trim()
        return when {
            limpio.isEmpty() -> "Ingresa tu nombre."
            limpio.length < 3 -> "El nombre debe tener al menos 3 letras."
            !SOLO_LETRAS.matches(limpio) -> "Usa solo letras y espacios."
            else -> null
        }
    }

    fun validarTelefono(telefono: String): String? {
        val limpio = normalizarTelefono(telefono)
        return when {
            limpio.isEmpty() -> "Ingresa tu celular."
            !CELULAR_CHILE.matches(limpio) -> "Celular no válido. Ejemplo: 9 1234 5678"
            else -> null
        }
    }

    /** Quita espacios, guiones y paréntesis que el usuario pueda escribir. */
    fun normalizarTelefono(telefono: String): String =
        telefono.filter { it.isDigit() || it == '+' }
}
