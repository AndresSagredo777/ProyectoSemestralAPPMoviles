package cl.duoc.mastermartiniapp.model

/**
 * Cuenta de acceso guardada en el dispositivo.
 * La contraseña nunca se guarda tal cual: se guarda su hash SHA-256.
 */
data class Cuenta(
    val nombre: String,
    val correo: String,
    val claveHash: String,
    val rol: Rol
)
