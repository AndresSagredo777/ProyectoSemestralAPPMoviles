package cl.duoc.mastermartiniapp.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.mastermartiniapp.R
import cl.duoc.mastermartiniapp.ui.theme.MasterMartiniApp_Grupo9Theme

@Composable
fun HomeScreenExpandida(
    onExplorarContenido: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(32.dp),

        horizontalArrangement = Arrangement.spacedBy(40.dp)
    ) {

        Box(
            modifier = Modifier
                .weight(1.15f)
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
        ) {

            Image(
                painter = painterResource(R.drawable.home_hero),
                contentDescription = "Pastelera preparando un producto",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alignment = Alignment.TopCenter
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.65f)
                            )
                        )
                    )
            )

            Text(
                text = "Master Martini",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(28.dp)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),

            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            EtiquetaPrincipalHome()
            Text(
                text = "Aprende, crea y potencia tu negocio",
                style = MaterialTheme.typography.headlineLarge
            )

            Text(
                text = "Recetas, cursos y contenido técnico para aprender\n" +
                        "y perfeccionar tus habilidades.",
                style = MaterialTheme.typography.bodyLarge
            )

            Button(
                onClick = onExplorarContenido,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Explorar contenido",
                    style = MaterialTheme.typography.labelLarge
                )
            }

            TituloSeccion(
                texto = "Explora nuestro contenido"
            )

            TiposContenido()

            TituloSeccion(
                texto = "Contenido destacado"
            )

            ContenidoDestacado()
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
        HomeScreenExpandida(
            onExplorarContenido = {}
        )
    }
}