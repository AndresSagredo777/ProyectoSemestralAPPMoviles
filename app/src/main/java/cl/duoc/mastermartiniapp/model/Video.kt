package cl.duoc.mastermartiniapp.model

data class Video(
    val id: Long = 0,
    val titulo: String,
    val descripcion: String,
    val categoria: String,
    val urlVideo: String
)