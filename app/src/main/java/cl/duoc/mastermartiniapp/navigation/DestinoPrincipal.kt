package cl.duoc.mastermartiniapp.navigation

import androidx.annotation.DrawableRes
import cl.duoc.mastermartiniapp.R
import cl.duoc.mastermartiniapp.model.Rol

/** Secciones que aparecen en la barra de navegación (inferior o lateral). */
enum class DestinoPrincipal(
    val ruta: Ruta,
    val etiqueta: String,
    @param:DrawableRes val icono: Int
) {
    INICIO(Ruta.Inicio, "Inicio", R.drawable.ic_home),
    CATEGORIAS(Ruta.Categorias, "Categorías", R.drawable.ic_categorias),
    RECETAS(Ruta.Recetas, "Recetas", R.drawable.ic_chef),
    ADMIN(Ruta.Admin, "Admin", R.drawable.ic_admin),
    PERFIL(Ruta.Perfil, "Perfil", R.drawable.ic_persona);

    /** Qué pestañas ve cada perfil: Usuario 3, Chef 4, Administrador 5. */
    fun visiblePara(rol: Rol): Boolean = when (this) {
        RECETAS -> rol.puedePublicarRecetas
        ADMIN -> rol.puedeGestionarCuentas
        else -> true
    }

    /** Indica si este destino debe verse seleccionado según la pantalla actual. */
    fun estaSeleccionado(rutaActual: String?): Boolean = when (this) {
        INICIO -> rutaActual == Ruta.Inicio.ruta
        // La lista de videos pertenece a la sección Categorías.
        CATEGORIAS -> rutaActual == Ruta.Categorias.ruta || rutaActual == Ruta.Videos.ruta
        RECETAS -> rutaActual == Ruta.Recetas.ruta
        ADMIN -> rutaActual == Ruta.Admin.ruta
        PERFIL -> rutaActual == Ruta.Perfil.ruta
    }
}
