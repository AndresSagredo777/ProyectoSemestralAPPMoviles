package cl.duoc.mastermartiniapp.model

/**
 * Perfiles de acceso de la app.
 * Cada rol define qué puede hacer el usuario; la navegación y las pantallas
 * consultan estas propiedades en vez de comparar textos.
 */
enum class Rol(
    val etiqueta: String,
    val descripcion: String
) {
    USUARIO(
        etiqueta = "Usuario",
        descripcion = "Ve el contenido, guarda favoritos y edita su perfil."
    ),
    CHEF(
        etiqueta = "Chef",
        descripcion = "Además publica sus propias recetas."
    ),
    ADMIN(
        etiqueta = "Administrador",
        descripcion = "Además gestiona las cuentas y sus roles."
    );

    /** Chef y Administrador pueden publicar recetas. */
    val puedePublicarRecetas: Boolean
        get() = this == CHEF || this == ADMIN

    /** Solo el Administrador gestiona cuentas. */
    val puedeGestionarCuentas: Boolean
        get() = this == ADMIN
}
