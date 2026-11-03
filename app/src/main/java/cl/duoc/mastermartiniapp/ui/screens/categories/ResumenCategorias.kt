package cl.duoc.mastermartiniapp.ui.screens.categories

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ResumenCategorias(total: Int) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = "$total especialidades disponibles",
            modifier = Modifier.padding(12.dp),
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}