package cl.duoc.mastermartiniapp.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TiposContenido() {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        EtiquetaContenido(
            texto = "Recetas",
            modifier = Modifier.weight(1f)
        )

        EtiquetaContenido(
            texto = "Cursos",
            modifier = Modifier.weight(1f)
        )

        EtiquetaContenido(
            texto = "Videos",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun EtiquetaContenido(
    texto: String,
    modifier: Modifier = Modifier
) {

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {

        Text(
            text = texto,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 9.dp
            )
        )
    }
}

@Composable
fun ContenidoDestacado() {

    Card(
        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(10.dp),

        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            Text(
                text = "Tarta intensa de chocolate",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Receta · Intermedio · 45 min",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}