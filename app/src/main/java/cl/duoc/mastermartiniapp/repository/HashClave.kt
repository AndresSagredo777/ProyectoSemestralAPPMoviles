package cl.duoc.mastermartiniapp.repository

import java.security.MessageDigest

/**
 * Convierte una contraseña en un hash SHA-256 (64 caracteres hexadecimales).
 * Así, aunque alguien lea las preferencias del teléfono, no ve la contraseña real.
 * Al iniciar sesión se compara el hash de lo escrito con el hash guardado.
 */
object HashClave {

    fun sha256(texto: String): String {
        val bytes = MessageDigest.getInstance("SHA-256")
            .digest(texto.toByteArray(Charsets.UTF_8))
        return bytes.joinToString(separator = "") { byte -> "%02x".format(byte) }
    }
}
