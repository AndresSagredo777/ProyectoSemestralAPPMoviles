package cl.duoc.mastermartiniapp.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.mastermartiniapp.R
import cl.duoc.mastermartiniapp.ui.theme.MasterMartiniApp_Grupo9Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenExpandida() {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Master Martini") }
            )
        }
    ) { innerPadding ->

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(32.dp),

            horizontalArrangement = Arrangement.spacedBy(40.dp)
        ) {

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                Image(
                    painter = painterResource(R.drawable.master_martini_logo),
                    contentDescription = "Logo Master Martini"
                )

                Text(
                    text = "Aprende, crea y potencia tu negocio",
                    style = MaterialTheme.typography.headlineLarge
                )

                Text(
                    text = "Contenido técnico para profesionales de la gastronomía.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text(
                    text = "Explora nuestro contenido",
                    style = MaterialTheme.typography.headlineMedium
                )

                Button(onClick = { }) {
                    Text("Recetas")
                }

                Button(onClick = { }) {
                    Text("Cursos")
                }

                Button(onClick = { }) {
                    Text("Videos")
                }

                Text(
                    text = "Contenido destacado",
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = "Tarta intensa de chocolate",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 1000,
    heightDp = 700
)
@Composable
fun HomeExpandidaPreview() {
    MasterMartiniApp_Grupo9Theme {
        HomeScreenExpandida()
    }
}