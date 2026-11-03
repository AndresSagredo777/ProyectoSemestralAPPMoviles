package cl.duoc.mastermartiniapp.model

/** Datos del perfil que se guardan localmente en el dispositivo. */
data class PerfilUsuario(
    val nombre: String = "",
    val telefono: String = "",
    val rutaFoto: String? = null
)
