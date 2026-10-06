package cl.duoc.mastermartiniapp.ui.screens.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.mastermartiniapp.model.Categoria
import cl.duoc.mastermartiniapp.ui.theme.MasterMartiniApp_Grupo9Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriasScreen() {

    val categorias = listOf(
        Categoria(
            nombre = "Chocolatería",
            descripcion = "Técnicas, recetas y preparaciones con chocolate"
        ),
        Categoria(
            nombre = "Pastelería",
            descripcion = "Postres y técnicas profesionales"
        ),
        Categoria(
            nombre = "Panadería",
            descripcion = "Masas, panes y procesos de horneado"
        ),
        Categoria(
            nombre = "Heladería",
            descripcion = "Preparaciones frías y técnicas de heladería"
        ),
        Categoria(
            nombre = "Bollería",
            descripcion = "Masas laminadas y productos de bollería"
        ),
        Categoria(
            nombre = "Galletas y horneo",
            descripcion = "Productos horneados y técnicas de cocción"
        )
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,

        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Categorías",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),

            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "APRENDE CON EXPERTOS",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium
            )

            Text(
                text = "¿Qué quieres aprender hoy?",
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "Elige una especialidad para descubrir recetas, técnicas y cursos.",
                style = MaterialTheme.typography.bodyLarge
            )

            categorias.forEach { categoria ->
                CategoriaCard(
                    categoria = categoria
                )
            }
        }
    }
}

@Composable
fun CategoriaCard(
    categoria: Categoria
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),

        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),

            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(12.dp)
                    )
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {

                Text(
                    text = categoria.nombre,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = categoria.descripcion,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 412,
    heightDp = 915
)
@Composable
fun CategoriasScreenPreview() {

    MasterMartiniApp_Grupo9Theme {
        CategoriasScreen()
    }
}