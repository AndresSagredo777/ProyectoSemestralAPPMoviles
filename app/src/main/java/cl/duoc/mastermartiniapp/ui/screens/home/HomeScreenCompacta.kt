package cl.duoc.mastermartiniapp.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.mastermartiniapp.R
import cl.duoc.mastermartiniapp.ui.theme.MasterMartiniApp_Grupo9Theme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenCompacta(
    onExplorarContenido: () -> Unit
) {

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,

        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Master Martini",
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
                .padding(16.dp),

            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Image(
                painter = painterResource(R.drawable.home_hero),
                contentDescription = "Pastelera preparando un producto",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Text(
                text = "Aprende, crea y potencia tu negocio",
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "Recetas, cursos y contenido técnico creado para profesionales como tú.",
                style = MaterialTheme.typography.bodyLarge
            )

            Button(
                onClick = onExplorarContenido,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Explorar contenido")
            }

            Text(
                text = "Explora nuestro contenido",
                style = MaterialTheme.typography.titleLarge
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Button(
                    onClick = { },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Recetas")
                }

                Button(
                    onClick = { },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cursos")
                }

                Button(
                    onClick = { },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Videos")
                }
            }

            Text(
                text = "Contenido destacado",
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "Tarta intensa de chocolate",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Intermedio · 45 min",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
fun HomeCompactaPreview() {
    MasterMartiniApp_Grupo9Theme {
        HomeScreenCompacta(
            onExplorarContenido = {}
        )
    }
}