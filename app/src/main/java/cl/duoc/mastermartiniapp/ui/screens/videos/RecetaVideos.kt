
package cl.duoc.mastermartiniapp.ui.screens.videos

data class RecetaVideo(
    val nombre: String,
    val ingredientes: List<String>,
)

fun obtenerRecetaVideo(idVideo: Long): RecetaVideo? {
    return when (idVideo) {
        1L -> RecetaVideo(
            nombre = "Crema de chocolate y avellanas",
            ingredientes = listOf(
                "300 g de chocolate Caravella Dark Disco",
                "100 g de crema Caravella Gran Nocciola",
                "350 ml de crema Cuisine Leve",
                "150 g de margarina Gran Master Horneo"
            )
        )

        2L -> RecetaVideo(
            nombre = "Bizcocho para tres leches de coco",
            ingredientes = listOf(
                "6 huevos (aprox. 380 g)",
                "360 g de harina",
                "60 g de maicena",
                "8 g de polvo de hornear",
                "190 g de azúcar",
                "30 ml de aceite",
                "10 ml de esencia de vainilla"
            )
        )

        3L -> RecetaVideo(
            nombre = "Gelato Fior di Latte (1 kg)",
            ingredientes = listOf(
                "584 g de leche entera",
                "205 g de crema de leche (35% materia grasa)",
                "36 g de leche descremada en polvo",
                "98 g de azúcar",
                "45 g de dextrosa",
                "28 g de azúcar invertido",
                "4 g de harina de algarroba (garrofín)"
            )
        )

        4L -> RecetaVideo(
            nombre = "Masa de hojaldre Master Export Hoja",
            ingredientes = listOf(
                "2 kg de margarina Master Export Hoja",
                "4 kg de harina de fuerza",
                "40 g de sal",
                "2,4 litros de agua"
            ),
        )

        else -> null
    }
}
