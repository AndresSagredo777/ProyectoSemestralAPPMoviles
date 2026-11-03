package cl.duoc.mastermartiniapp.ui.screens.chef

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.mastermartiniapp.R
import cl.duoc.mastermartiniapp.model.Cuenta
import cl.duoc.mastermartiniapp.model.RecetaChef
import cl.duoc.mastermartiniapp.ui.components.CampoValidado
import cl.duoc.mastermartiniapp.ui.screens.home.TituloSeccion
import cl.duoc.mastermartiniapp.viewmodel.ChefUiState
import cl.duoc.mastermartiniapp.viewmodel.ChefViewModel
import cl.duoc.mastermartiniapp.viewmodel.ValidadorReceta

/**
 * Panel del Chef (roles CHEF y ADMIN).
 * Formulario "Publicar receta" con validación y lista de recetas guardadas en DataStore.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChefScreen(
    cuentaActual: Cuenta,
    chefViewModel: ChefViewModel = viewModel()
) {
    val uiState by chefViewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.mensaje) {
        uiState.mensaje?.let {
            snackbarHostState.showSnackbar(it)
            chefViewModel.mensajeMostrado()
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { TopAppBar(title = { Text("Recetas del chef") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                FormularioReceta(
                    uiState = uiState,
                    onTituloChange = chefViewModel::onTituloChange,
                    onCategoriaChange = chefViewModel::onCategoriaChange,
                    onIngredientesChange = chefViewModel::onIngredientesChange,
                    onPublicar = { chefViewModel.publicar(cuentaActual) },
                    modifier = Modifier.widthIn(max = 640.dp)
                )
            }

            item {
                Column(modifier = Modifier.widthIn(max = 640.dp).fillMaxWidth()) {
                    TituloSeccion(texto = "Recetas publicadas (${uiState.recetas.size})")
                }
            }

            if (uiState.recetas.isEmpty()) {
                item {
                    Text(
                        text = "Todavía no hay recetas. Publica la primera con el formulario.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.widthIn(max = 640.dp).fillMaxWidth()
                    )
                }
            }

            items(uiState.recetas, key = { it.id }) { receta ->
                RecetaItem(
                    receta = receta,
                    puedeEliminar = receta.autorCorreo == cuentaActual.correo ||
                        cuentaActual.rol.puedeGestionarCuentas,
                    onEliminar = { chefViewModel.eliminar(receta, cuentaActual) },
                    modifier = Modifier.widthIn(max = 640.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FormularioReceta(
    uiState: ChefUiState,
    onTituloChange: (String) -> Unit,
    onCategoriaChange: (String) -> Unit,
    onIngredientesChange: (String) -> Unit,
    onPublicar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TituloSeccion(texto = "Publicar receta")

        CampoValidado(
            valor = uiState.titulo,
            onValorChange = onTituloChange,
            etiqueta = "Nombre de la receta",
            icono = R.drawable.ic_chef,
            error = uiState.tituloError,
            ayuda = "Ejemplo: Brownie de chocolate amargo"
        )

        Text(
            text = "Categoría",
            style = MaterialTheme.typography.labelLarge,
            color = if (uiState.categoriaError != null) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ValidadorReceta.CATEGORIAS.forEach { categoria ->
                FilterChip(
                    selected = uiState.categoria == categoria,
                    onClick = { onCategoriaChange(categoria) },
                    label = { Text(categoria) }
                )
            }
        }
        AnimatedVisibility(visible = uiState.categoriaError != null) {
            Text(
                text = uiState.categoriaError.orEmpty(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        CampoValidado(
            valor = uiState.ingredientesTexto,
            onValorChange = onIngredientesChange,
            etiqueta = "Ingredientes",
            icono = R.drawable.ic_agregar,
            error = uiState.ingredientesError,
            ayuda = "Uno por línea. Mínimo ${ValidadorReceta.MINIMO_INGREDIENTES}.",
            accionTeclado = ImeAction.Default,
            lineas = 4
        )

        Button(
            onClick = onPublicar,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_agregar),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text("Publicar receta")
        }

        AnimatedVisibility(
            visible = uiState.publicada,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Receta publicada y guardada en el dispositivo.",
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun RecetaItem(
    receta: RecetaChef,
    puedeEliminar: Boolean,
    onEliminar: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expandida by rememberSaveable(receta.id) { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = receta.categoria.uppercase(),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(text = receta.titulo, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = "Por ${receta.autorNombre} · ${receta.ingredientes.size} ingredientes",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (puedeEliminar) {
                    IconButton(onClick = onEliminar) {
                        Icon(
                            painter = painterResource(R.drawable.ic_eliminar),
                            contentDescription = "Eliminar ${receta.titulo}",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            TextButton(onClick = { expandida = !expandida }) {
                Text(if (expandida) "Ocultar ingredientes" else "Ver ingredientes")
            }

            if (expandida) {
                receta.ingredientes.forEach { ingrediente ->
                    Text(text = "• $ingrediente", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
