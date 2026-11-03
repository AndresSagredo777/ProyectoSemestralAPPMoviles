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
}
