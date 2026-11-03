package cl.duoc.mastermartiniapp.viewmodel

/** Reglas del formulario "Publicar receta" del Chef. */
object ValidadorReceta {

    const val MINIMO_INGREDIENTES = 2

    /** Categorías disponibles (las mismas que entrega el backend). */
    val CATEGORIAS = listOf(
        "Chocolatería",
        "Pastelería",
        "Panadería",
        "Heladería",
        "Bollería",
        "Galletas y horneo"
    )

    fun validarTitulo(titulo: String): String? {
        val limpio = titulo.trim()
        return when {
            limpio.isEmpty() -> "Escribe el nombre de la receta."
            limpio.length < 4 -> "El nombre debe tener al menos 4 caracteres."
            limpio.length > 60 -> "Máximo 60 caracteres."
            else -> null
        }
    }

    fun validarCategoria(categoria: String): String? =
        if (categoria !in CATEGORIAS) "Elige una categoría." else null

    fun validarIngredientes(texto: String): String? {
        val cantidad = separarIngredientes(texto).size
        return if (cantidad < MINIMO_INGREDIENTES) {
            "Agrega al menos $MINIMO_INGREDIENTES ingredientes (uno por línea)."
        } else {
            null
        }
    }

    /** Convierte el texto (un ingrediente por línea) en una lista limpia. */
    fun separarIngredientes(texto: String): List<String> =
        texto.lines().map { it.trim() }.filter { it.isNotEmpty() }
}
