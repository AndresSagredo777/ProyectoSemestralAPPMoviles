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
fun HomeScreenMediana(
    onExplorarContenido: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(
                    RoundedCornerShape(
                        bottomStart = 6.dp,
                        bottomEnd = 6.dp
                    )
                )
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
                                Color.Black.copy(alpha = 0.60f)
                            )
                        )
                    )
            )

            Text(
                text = "Master Martini",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(28.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),

            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ) {

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
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
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Explorar contenido",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    TituloSeccion(
                        texto = "Explora nuestro contenido"
                    )

                    TiposContenido()
                }
            }

            TituloSeccion(
                texto = "Contenido destacado"
            )

            ContenidoDestacado()
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
        HomeScreenMediana(
            onExplorarContenido = {}
        )
    }
}