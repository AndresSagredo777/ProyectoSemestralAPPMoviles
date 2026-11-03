package cl.duoc.mastermartiniapp.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import cl.duoc.mastermartiniapp.R

/**
 * Campo de texto reutilizable con validación visual.
 *
 * - icono: ícono a la izquierda que indica qué dato se pide.
 * - error: si no es null, el borde se pone rojo, aparece el ícono de error
 *   y el mensaje reemplaza al texto de ayuda.
 * - esClave: oculta el texto y agrega el botón del ojo para mostrarlo.
 * - lineas: más de 1 permite texto en varias líneas (ej. ingredientes).
 *
 * El campo no valida nada por sí mismo: solo muestra el error que le
 * entrega el ViewModel (la lógica queda fuera del Composable).
 */
@Composable
fun CampoValidado(
    valor: String,
    onValorChange: (String) -> Unit,
    etiqueta: String,
    @DrawableRes icono: Int,
    error: String?,
    modifier: Modifier = Modifier,
    ayuda: String? = null,
    tipoTeclado: KeyboardType = KeyboardType.Text,
    accionTeclado: ImeAction = ImeAction.Next,
    esClave: Boolean = false,
    mostrarClave: Boolean = false,
    onAlternarClave: () -> Unit = {},
    lineas: Int = 1
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValorChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text(etiqueta) },
        singleLine = lineas == 1,
        minLines = lineas,
        isError = error != null,
        leadingIcon = {
            Icon(painter = painterResource(icono), contentDescription = null)
        },
        trailingIcon = when {
            esClave -> {
                {
                    IconButton(onClick = onAlternarClave) {
                        Icon(
                            painter = painterResource(
                                if (mostrarClave) R.drawable.ic_oculto else R.drawable.ic_visible
                            ),
                            contentDescription = if (mostrarClave) "Ocultar contraseña" else "Mostrar contraseña",
                            tint = if (error != null) {
                                MaterialTheme.colorScheme.error
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    }
                }
            }
            error != null -> {
                {
                    Icon(
                        painter = painterResource(R.drawable.ic_error),
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
            else -> null
        },
        supportingText = if (error != null || ayuda != null) {
            { Text(error ?: ayuda.orEmpty()) }
        } else {
            null
        },
        visualTransformation = if (esClave && !mostrarClave) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = tipoTeclado,
            imeAction = accionTeclado
        ),
        shape = RoundedCornerShape(8.dp)
    )
}
