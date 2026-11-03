package cl.duoc.mastermartiniapp.ui.screens.acceso

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.mastermartiniapp.R
import cl.duoc.mastermartiniapp.model.Cuenta
import cl.duoc.mastermartiniapp.repository.CuentasRepository
import cl.duoc.mastermartiniapp.ui.components.CampoValidado
import cl.duoc.mastermartiniapp.ui.components.RolBadge
import cl.duoc.mastermartiniapp.ui.theme.MasterMartiniApp_Grupo9Theme
import cl.duoc.mastermartiniapp.viewmodel.AccesoUiState
import cl.duoc.mastermartiniapp.viewmodel.AccesoViewModel

/** Contraseñas de las cuentas de prueba (solo para mostrarlas en la presentación). */
private val CLAVES_DEMO = mapOf(
    "admin@mastermartini.cl" to "Admin123",
    "chef@mastermartini.cl" to "Chef123",
    "usuario@mastermartini.cl" to "Usuario123"
)

/**
 * Pantalla de acceso: iniciar sesión o crear cuenta.
 * Solo dibuja el estado de AccesoViewModel y le avisa lo que hace el usuario.
 */
@Composable
fun AccesoScreen(
    accesoViewModel: AccesoViewModel = viewModel()
) {
    val uiState by accesoViewModel.uiState.collectAsState()

    AccesoContenido(
        uiState = uiState,
        onNombreChange = accesoViewModel::onNombreChange,
        onCorreoChange = accesoViewModel::onCorreoChange,
        onClaveChange = accesoViewModel::onClaveChange,
        onConfirmacionChange = accesoViewModel::onConfirmacionChange,
        onAlternarClave = accesoViewModel::alternarMostrarClave,
        onEnviar = accesoViewModel::enviar,
        onAlternarModo = accesoViewModel::alternarModo,
        onUsarDemo = { cuenta ->
            accesoViewModel.usarCuentaDemo(cuenta.correo, CLAVES_DEMO[cuenta.correo].orEmpty())
        }
    )
}

@Composable
fun AccesoContenido(
    uiState: AccesoUiState,
    onNombreChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onClaveChange: (String) -> Unit,
    onConfirmacionChange: (String) -> Unit,
    onAlternarClave: () -> Unit,
    onEnviar: () -> Unit,
    onAlternarModo: () -> Unit,
    onUsarDemo: (Cuenta) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding()
            .imePadding(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 480.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp)
                .animateContentSize(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "MASTER MARTINI",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )

            // El título cambia con una transición al pasar de "Iniciar sesión" a "Crear cuenta".
            AnimatedContent(
                targetState = uiState.modoRegistro,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "tituloAcceso"
            ) { registro ->
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = if (registro) "Crea tu cuenta" else "Bienvenido de vuelta",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = if (registro) {
                            "Las cuentas nuevas parten con el perfil Usuario."
                        } else {
                            "Ingresa para ver recetas, cursos y videos."
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            AnimatedVisibility(
                visible = uiState.modoRegistro,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                CampoValidado(
                    valor = uiState.nombre,
                    onValorChange = onNombreChange,
                    etiqueta = "Nombre",
                    icono = R.drawable.ic_persona,
                    error = uiState.nombreError
                )
            }

            CampoValidado(
                valor = uiState.correo,
                onValorChange = onCorreoChange,
                etiqueta = "Correo electrónico",
                icono = R.drawable.ic_correo,
                error = uiState.correoError,
                tipoTeclado = KeyboardType.Email
            )

            CampoValidado(
                valor = uiState.clave,
                onValorChange = onClaveChange,
                etiqueta = "Contraseña",
                icono = R.drawable.ic_candado,
                error = uiState.claveError,
                ayuda = if (uiState.modoRegistro) "Mínimo 6 caracteres, con letras y números" else null,
                tipoTeclado = KeyboardType.Password,
                accionTeclado = if (uiState.modoRegistro) ImeAction.Next else ImeAction.Done,
                esClave = true,
                mostrarClave = uiState.mostrarClave,
                onAlternarClave = onAlternarClave
            )

            AnimatedVisibility(
                visible = uiState.modoRegistro,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                CampoValidado(
                    valor = uiState.confirmacion,
                    onValorChange = onConfirmacionChange,
                    etiqueta = "Repite la contraseña",
                    icono = R.drawable.ic_candado,
                    error = uiState.confirmacionError,
                    tipoTeclado = KeyboardType.Password,
                    accionTeclado = ImeAction.Done,
                    esClave = true,
                    mostrarClave = uiState.mostrarClave,
                    onAlternarClave = onAlternarClave
                )
            }

            // Error que no pertenece a un campo (ej. contraseña incorrecta).
            AnimatedVisibility(
                visible = uiState.errorGeneral != null,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_error),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = uiState.errorGeneral.orEmpty(),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }

            Button(
                onClick = onEnviar,
                enabled = !uiState.cargando,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                if (uiState.cargando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(Modifier.width(10.dp))
                    Text("Verificando...")
                } else {
                    Text(if (uiState.modoRegistro) "Crear cuenta" else "Iniciar sesión")
                }
            }

            TextButton(
                onClick = onAlternarModo,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = if (uiState.modoRegistro) {
                        "¿Ya tienes cuenta? Inicia sesión"
                    } else {
                        "¿No tienes cuenta? Créala aquí"
                    },
                    textAlign = TextAlign.Center
                )
            }

            if (!uiState.modoRegistro) {
                CuentasDemo(onUsarDemo = onUsarDemo)
            }
        }
    }
}

/** Atajos para probar los 3 perfiles durante la presentación. */
@Composable
private fun CuentasDemo(onUsarDemo: (Cuenta) -> Unit) {
    OutlinedCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Cuentas de prueba",
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = "Toca una para rellenar el formulario.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            HorizontalDivider()
            CuentasRepository.CUENTAS_DEMO.forEach { cuenta ->
                TextButton(
                    onClick = { onUsarDemo(cuenta) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        RolBadge(rol = cuenta.rol, compacto = true)
                        Text(
                            text = cuenta.correo,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun AccesoPreview() {
    MasterMartiniApp_Grupo9Theme {
        AccesoContenido(
            uiState = AccesoUiState(correo = "chef@", correoError = "Correo no válido. Ejemplo: nombre@correo.cl"),
            onNombreChange = {},
            onCorreoChange = {},
            onClaveChange = {},
            onConfirmacionChange = {},
            onAlternarClave = {},
            onEnviar = {},
            onAlternarModo = {},
            onUsarDemo = {}
        )
    }
}
