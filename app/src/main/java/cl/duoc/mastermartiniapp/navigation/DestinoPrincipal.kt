package cl.duoc.mastermartiniapp.navigation

import androidx.annotation.DrawableRes
import cl.duoc.mastermartiniapp.R

/** Secciones que aparecen en la barra de navegación (inferior o lateral). */
enum class DestinoPrincipal(
    val ruta: Ruta,
    val etiqueta: String,
    @param:DrawableRes val icono: Int
) {
    INICIO(Ruta.Inicio, "Inicio", R.drawable.ic_home),
    CATEGORIAS(Ruta.Categorias, "Categorías", R.drawable.ic_categorias),
    PERFIL(Ruta.Perfil, "Perfil", R.drawable.ic_persona);

    /** Indica si este destino debe verse seleccionado según la pantalla actual. */
    fun estaSeleccionado(rutaActual: String?): Boolean = when (this) {
        INICIO -> rutaActual == Ruta.Inicio.ruta
        // La lista de videos pertenece a la sección Categorías.
        CATEGORIAS -> rutaActual == Ruta.Categorias.ruta || rutaActual == Ruta.Videos.ruta
        PERFIL -> rutaActual == Ruta.Perfil.ruta
    }
}
