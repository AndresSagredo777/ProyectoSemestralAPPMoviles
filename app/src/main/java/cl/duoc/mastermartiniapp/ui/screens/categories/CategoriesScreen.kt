package cl.duoc.mastermartiniapp.ui.screens.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.mastermartiniapp.model.Categoria
import cl.duoc.mastermartiniapp.ui.theme.MasterMartiniApp_Grupo9Theme
import cl.duoc.mastermartiniapp.viewmodel.CategoriasUiState
import cl.duoc.mastermartiniapp.viewmodel.CategoriasViewModel

@Composable
fun CategoriasScreen(
    categoriasViewModel: CategoriasViewModel = viewModel()
) {

    val uiState by categoriasViewModel.uiState.collectAsState()

    CategoriasContenido(
        uiState = uiState,
        onReintentar = {
            categoriasViewModel.cargarCategorias()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriasContenido(
    uiState: CategoriasUiState,
    onReintentar: () -> Unit
) {

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,

        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Categorías",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        }
    ) { innerPadding ->

        when {

            uiState.cargando -> {

                EstadoCargando(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }

            uiState.mensajeError != null -> {

                EstadoError(
                    mensaje = uiState.mensajeError,
                    onReintentar = onReintentar,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }

            uiState.categorias.isEmpty() -> {

                EstadoVacio(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }

            else -> {

                ListaCategorias(
                    categorias = uiState.categorias,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }
        }
    }
}

@Composable
fun ListaCategorias(
    categorias: List<Categoria>,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),

        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "APRENDE CON EXPERTOS",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelMedium
        )

        Text(
            text = "¿Qué quieres aprender hoy?",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Elige una especialidad para descubrir recetas, técnicas y cursos.",
            style = MaterialTheme.typography.bodyLarge
        )

        categorias.forEach { categoria ->

            CategoriaCard(
                categoria = categoria
            )
        }
    }
}

@Composable
fun CategoriaCard(
    categoria: Categoria
) {

    Card(
        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(16.dp),

        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),

            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(12.dp)
                    )
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {

                Text(
                    text = categoria.nombre,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = categoria.descripcion,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun EstadoCargando(
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {

        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun EstadoError(
    mensaje: String,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.padding(24.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Ocurrió un problema",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.error
        )

        Text(
            text = mensaje,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 8.dp)
        )

        Button(
            onClick = onReintentar,
            modifier = Modifier.padding(top = 16.dp)
        ) {

            Text(
                text = "Reintentar"
            )
        }
    }
}

@Composable
fun EstadoVacio(
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier.padding(24.dp),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = "Aún no hay categorías disponibles.",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(
    name = "Categorías - Éxito",
    showBackground = true,
    widthDp = 412,
    heightDp = 915
)
@Composable
fun CategoriasExitoPreview() {

    MasterMartiniApp_Grupo9Theme {

        CategoriasContenido(
            uiState = CategoriasUiState(
                categorias = listOf(
                    Categoria(
                        nombre = "Chocolatería",
                        descripcion = "Técnicas, recetas y preparaciones con chocolate"
                    ),
                    Categoria(
                        nombre = "Pastelería",
                        descripcion = "Postres y técnicas profesionales"
                    ),
                    Categoria(
                        nombre = "Panadería",
                        descripcion = "Masas, panes y procesos de horneado"
                    )
                )
            ),
            onReintentar = {}
        )
    }
}

@Preview(
    name = "Categorías - Cargando",
    showBackground = true,
    widthDp = 412,
    heightDp = 915
)
@Composable
fun CategoriasCargandoPreview() {

    MasterMartiniApp_Grupo9Theme {

        CategoriasContenido(
            uiState = CategoriasUiState(
                cargando = true
            ),
            onReintentar = {}
        )
    }
}

@Preview(
    name = "Categorías - Vacío",
    showBackground = true,
    widthDp = 412,
    heightDp = 915
)
@Composable
fun CategoriasVacioPreview() {

    MasterMartiniApp_Grupo9Theme {

        CategoriasContenido(
            uiState = CategoriasUiState(
                categorias = emptyList()
            ),
            onReintentar = {}
        )
    }
}

@Preview(
    name = "Categorías - Error",
    showBackground = true,
    widthDp = 412,
    heightDp = 915
)
@Composable
fun CategoriasErrorPreview() {

    MasterMartiniApp_Grupo9Theme {

        CategoriasContenido(
            uiState = CategoriasUiState(
                mensajeError = "No se pudieron cargar las categorías."
            ),
            onReintentar = {}
        )
    }
}