package cl.duoc.mastermartiniapp.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
fun HomeScreenMediana() {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Master Martini") }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),

            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                Image(
                    painter = painterResource(R.drawable.master_martini_logo),
                    contentDescription = "Logo Master Martini",
                    modifier = Modifier.size(180.dp)
                )

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Text(
                        text = "Aprende, crea y potencia tu negocio",
                        style = MaterialTheme.typography.headlineLarge
                    )

                    Text(
                        text = "Recetas, cursos y contenido técnico creado para profesionales como tú.",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Button(
                        onClick = { }
                    ) {
                        Text("Explorar contenido")
                    }
                }
            }

            Text(
                text = "Explora nuestro contenido",
                style = MaterialTheme.typography.titleLarge
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Button({ }, Modifier.weight(1f)) {
                    Text("Recetas")
                }

                Button({ }, Modifier.weight(1f)) {
                    Text("Cursos")
                }

                Button({ }, Modifier.weight(1f)) {
                    Text("Videos")
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 700,
    heightDp = 900
)
@Composable
fun HomeMedianaPreview() {
    MasterMartiniApp_Grupo9Theme {
        HomeScreenMediana()
    }
}