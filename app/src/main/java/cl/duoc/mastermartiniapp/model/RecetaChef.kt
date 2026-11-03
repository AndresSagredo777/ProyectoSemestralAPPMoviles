package cl.duoc.mastermartiniapp.model

/** Receta publicada por un Chef (o Administrador) desde la app. */
data class RecetaChef(
    val id: Long,
    val titulo: String,
    val categoria: String,
    val ingredientes: List<String>,
    val autorCorreo: String,
    val autorNombre: String
)
