package cl.duoc.mastermartiniapp.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.mastermartiniapp.R
import cl.duoc.mastermartiniapp.model.Rol
import cl.duoc.mastermartiniapp.ui.theme.MasterMartiniApp_Grupo9Theme

/**
 * Etiqueta reutilizable que muestra el rol de una cuenta.
 *
 * Parámetros:
 * - rol: define texto, ícono y colores.
 * - compacto: true muestra solo el texto (para listas).
 *
 * Animaciones: si el rol cambia (por ejemplo, el Admin asciende a alguien a Chef),
 * el color de fondo se anima con animateColorAsState y el texto con AnimatedContent.
 */
@Composable
fun RolBadge(
    rol: Rol,
    modifier: Modifier = Modifier,
    compacto: Boolean = false
) {
    val colores = MaterialTheme.colorScheme
    val (fondoObjetivo: Color, textoObjetivo: Color) = when (rol) {
        Rol.ADMIN -> colores.primary to colores.onPrimary
        Rol.CHEF -> colores.secondaryContainer to colores.onSecondaryContainer
        Rol.USUARIO -> colores.surfaceVariant to colores.onSurfaceVariant
    }

    val fondo by animateColorAsState(fondoObjetivo, tween(durationMillis = 400), label = "fondoRol")
    val texto by animateColorAsState(textoObjetivo, tween(durationMillis = 400), label = "textoRol")

    Surface(
        modifier = modifier,
        color = fondo,
        contentColor = texto,
        shape = RoundedCornerShape(50)
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = if (compacto) 10.dp else 12.dp,
                vertical = if (compacto) 3.dp else 6.dp
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (!compacto) {
                Icon(
                    painter = painterResource(iconoDe(rol)),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
            AnimatedContent(
                targetState = rol,
                transitionSpec = { fadeIn(tween(250)) togetherWith fadeOut(tween(250)) },
                label = "textoRol"
            ) { rolActual ->
                Text(
                    text = rolActual.etiqueta,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@DrawableRes
fun iconoDe(rol: Rol): Int = when (rol) {
    Rol.ADMIN -> R.drawable.ic_admin
    Rol.CHEF -> R.drawable.ic_chef
    Rol.USUARIO -> R.drawable.ic_persona
}

@Preview(showBackground = true)
@Composable
private fun RolBadgePreview() {
    MasterMartiniApp_Grupo9Theme {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(8.dp)) {
            Rol.entries.forEach { RolBadge(rol = it) }
        }
    }
}
