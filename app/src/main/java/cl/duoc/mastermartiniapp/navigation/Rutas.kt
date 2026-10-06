package cl.duoc.mastermartiniapp.navigation

sealed class Ruta(val ruta: String) {

    object Inicio : Ruta("inicio")

    object Categorias : Ruta("categorias")
}

