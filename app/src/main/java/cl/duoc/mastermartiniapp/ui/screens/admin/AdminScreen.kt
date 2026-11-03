package cl.duoc.mastermartiniapp.ui.screens.admin

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.mastermartiniapp.R
import cl.duoc.mastermartiniapp.model.Cuenta
import cl.duoc.mastermartiniapp.model.Rol
import cl.duoc.mastermartiniapp.ui.components.RolBadge
import cl.duoc.mastermartiniapp.viewmodel.AdminViewModel

/**
 * Panel de Administración (solo rol ADMIN).
 * Muestra todas las cuentas guardadas en el dispositivo y permite
 * cambiar su rol o eliminarlas. Las reglas viven en AdminViewModel.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    cuentaActual: Cuenta,
    adminViewModel: AdminViewModel = viewModel()
) {
    val cuentas by adminViewModel.cuentas.collectAsState()
    val mensaje by adminViewModel.mensaje.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var cuentaPorEliminar by remember { mutableStateOf<Cuenta?>(null) }

    LaunchedEffect(mensaje) {
        mensaje?.let {
            snackbarHostState.showSnackbar(it)
            adminViewModel.mensajeMostrado()
        }
    }

    cuentaPorEliminar?.let { cuenta ->
        AlertDialog(
            onDismissRequest = { cuentaPorEliminar = null },
            title = { Text("Eliminar cuenta") },
            text = { Text("¿Eliminar la cuenta de ${cuenta.nombre} (${cuenta.correo})? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    adminViewModel.eliminar(cuenta, cuentaActual.correo)
                    cuentaPorEliminar = null
                }) { Text("Eliminar", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { cuentaPorEliminar = null }) { Text("Cancelar") }
            }
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { TopAppBar(title = { Text("Administración") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                ResumenRoles(
                    cuentas = cuentas,
                    modifier = Modifier.widthIn(max = 640.dp)
                )
            }
            items(cuentas, key = { it.correo }) { cuenta ->
                CuentaItem(
                    cuenta = cuenta,
                    esCuentaActual = cuenta.correo == cuentaActual.correo,
                    onCambiarRol = { rol -> adminViewModel.cambiarRol(cuenta, rol, cuentaActual.correo) },
                    onEliminar = { cuentaPorEliminar = cuenta },
                    modifier = Modifier.widthIn(max = 640.dp)
                )
            }
        }
    }
}

@Composable
private fun ResumenRoles(cuentas: List<Cuenta>, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "${cuentas.size} cuentas en este dispositivo",
            style = MaterialTheme.typography.titleMedium
        )
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Rol.entries.forEach { rol ->
                val cantidad = cuentas.count { it.rol == rol }
                Text(
                    text = "${rol.etiqueta}: $cantidad",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CuentaItem(
    cuenta: Cuenta,
    esCuentaActual: Boolean,
    onCambiarRol: (Rol) -> Unit,
    onEliminar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (esCuentaActual) "${cuenta.nombre} (tú)" else cuenta.nombre,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = cuenta.correo,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                RolBadge(rol = cuenta.rol)
                IconButton(onClick = onEliminar, enabled = !esCuentaActual) {
                    Icon(
                        painter = painterResource(R.drawable.ic_eliminar),
                        contentDescription = "Eliminar cuenta de ${cuenta.nombre}",
                        tint = if (esCuentaActual) {
                            MaterialTheme.colorScheme.outline
                        } else {
                            MaterialTheme.colorScheme.error
                        }
                    )
                }
            }

            Text(
                text = "Cambiar rol",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Rol.entries.forEach { rol ->
                    FilterChip(
                        selected = cuenta.rol == rol,
                        onClick = { onCambiarRol(rol) },
                        enabled = !esCuentaActual,
                        label = { Text(rol.etiqueta) }
                    )
                }
            }
        }
    }
}
