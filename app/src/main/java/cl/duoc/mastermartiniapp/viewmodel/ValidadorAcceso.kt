package cl.duoc.mastermartiniapp.viewmodel

/**
 * Reglas de validación de inicio de sesión y registro.
 * Kotlin puro (sin Compose ni Android), por eso se puede probar con JUnit.
 * Cada función devuelve el mensaje de error, o null si el dato es válido.
 */
object ValidadorAcceso {

    private val FORMATO_CORREO = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    const val LARGO_MINIMO_CLAVE = 6

    fun validarCorreo(correo: String): String? {
        val limpio = correo.trim()
        return when {
            limpio.isEmpty() -> "Ingresa tu correo."
            !FORMATO_CORREO.matches(limpio) -> "Correo no válido. Ejemplo: nombre@correo.cl"
            else -> null
        }
    }

    /** Al iniciar sesión solo se exige que no esté vacía. */
    fun validarClaveIngreso(clave: String): String? =
        if (clave.isEmpty()) "Ingresa tu contraseña." else null

    /** Al crear una cuenta se exige una contraseña más segura. */
    fun validarClaveNueva(clave: String): String? = when {
        clave.isEmpty() -> "Crea una contraseña."
        clave.length < LARGO_MINIMO_CLAVE -> "Debe tener al menos $LARGO_MINIMO_CLAVE caracteres."
        clave.none { it.isLetter() } || clave.none { it.isDigit() } -> "Debe combinar letras y números."
        clave.contains(' ') -> "No puede tener espacios."
        else -> null
    }

    fun validarConfirmacion(clave: String, confirmacion: String): String? = when {
        confirmacion.isEmpty() -> "Repite tu contraseña."
        confirmacion != clave -> "Las contraseñas no coinciden."
        else -> null
    }

    /** El nombre usa la misma regla que el formulario de perfil. */
    fun validarNombre(nombre: String): String? = ValidadorPerfil.validarNombre(nombre)
}
