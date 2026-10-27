package cl.duoc.mastermartiniapp.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import cl.duoc.mastermartiniapp.viewmodel.FormularioUiState

@Composable
fun FormularioContacto(
    state: FormularioUiState,
    onNombreChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onMensajeChange: (String) -> Unit,
    onEnviar: () -> Unit,
    onLimpiarConfirmacion: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (state.enviado) {
        LaunchedEffect(state.enviado) {
            delay(3500)
            onLimpiarConfirmacion()
        }
    }

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        TituloSeccion("¿Tienes una consulta?")
        Text("Déjanos tus datos y cuéntanos cómo podemos ayudarte.", style = MaterialTheme.typography.bodyMedium)
        CampoFormulario(
            valor = state.nombre,
            etiqueta = "Nombre",
            error = state.nombreError,
            onValorChange = onNombreChange,
            icono = { Text("👤") }
        )
        CampoFormulario(
            valor = state.correo,
            etiqueta = "Correo electrónico",
            error = state.correoError,
            onValorChange = onCorreoChange,
            keyboardType = KeyboardType.Email,
            icono = { Text("✉️") }
        )
        OutlinedTextField(
            value = state.mensaje,
            onValueChange = onMensajeChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Mensaje") },
            minLines = 3,
            isError = state.mensajeError != null,
            supportingText = state.mensajeError?.let { { Text(it) } },
            trailingIcon = state.mensajeError?.let { { Text("⚠️") } }
        )
        Button(onClick = onEnviar, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
            Text("➤")
            Text("  Enviar consulta")
        }
        AnimatedVisibility(visible = state.enviado, enter = fadeIn() + slideInVertically()) {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("✓", color = MaterialTheme.colorScheme.primary)
                    Text("Formulario validado. ¡Gracias por tu consulta!", color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }
        }
    }
}

@Composable
private fun CampoFormulario(
    valor: String,
    etiqueta: String,
    error: String?,
    onValorChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    icono: @Composable () -> Unit
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValorChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(etiqueta) },
        leadingIcon = icono,
        isError = error != null,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        supportingText = error?.let { { Text(it) } },
        trailingIcon = error?.let { { Text("⚠️") } }
    )
}
