package cl.duoc.mastermartiniapp.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cl.duoc.mastermartiniapp.R
import cl.duoc.mastermartiniapp.ui.theme.MasterMartiniApp_Grupo9Theme

/**
 * Componente reutilizable que muestra la foto de perfil en forma circular.
 * - Sin foto: muestra un ícono de persona por defecto.
 * - Con foto: aparece con una transición suave (Crossfade) y el borde cambia a dorado.
 * - Mientras se procesa la imagen: muestra un indicador de carga encima.
 */
@Composable
fun ImagenInteligente(
    imagen: ImageBitmap?,
    cargando: Boolean,
    modifier: Modifier = Modifier,
    tamano: Dp = 140.dp
) {
    val colorBorde by animateColorAsState(
        targetValue = if (imagen != null) {
            MaterialTheme.colorScheme.secondary
        } else {
            MaterialTheme.colorScheme.outline
        },
        animationSpec = tween(durationMillis = 600),
        label = "colorBordeFoto"
    )

    Box(
        modifier = modifier
            .size(tamano)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .border(width = 4.dp, color = colorBorde, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Crossfade(
            targetState = imagen,
            animationSpec = tween(durationMillis = 500),
            label = "transicionFoto"
        ) { foto ->
            if (foto != null) {
                Image(
                    bitmap = foto,
                    contentDescription = "Foto de perfil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(R.drawable.ic_persona),
                        contentDescription = "Sin foto de perfil",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(tamano * 0.55f)
                    )
                }
            }
        }

        if (cargando) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.35f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ImagenInteligenteSinFotoPreview() {
    MasterMartiniApp_Grupo9Theme {
        ImagenInteligente(imagen = null, cargando = false)
    }
}
