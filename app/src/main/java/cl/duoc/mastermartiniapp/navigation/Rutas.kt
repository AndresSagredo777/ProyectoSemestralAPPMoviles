package cl.duoc.mastermartiniapp.navigation

import android.net.Uri

sealed class Ruta(val ruta: String) {

    object Inicio : Ruta("inicio")

    object Categorias : Ruta("categorias")

    object Videos : Ruta("videos/{categoria}") {

        fun crearRuta(categoria: String): String {
            return "videos/${Uri.encode(categoria)}"
        }
    }

    object Perfil : Ruta("perfil")

    /** Panel del Chef: publicar recetas (CHEF y ADMIN). */
    object Recetas : Ruta("recetas")

    /** Panel de Administración: gestionar cuentas (solo ADMIN). */
    object Admin : Ruta("admin")
}
