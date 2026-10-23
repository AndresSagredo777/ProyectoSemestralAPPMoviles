package cl.duoc.mastermartiniapp.ui.screens.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as itemsGrid
import androidx.compose.foundation.lazy.items as itemsLista
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.mastermartiniapp.model.Categoria
import cl.duoc.mastermartiniapp.ui.theme.MasterMartiniApp_Grupo9Theme
import cl.duoc.mastermartiniapp.ui.utils.obtenerWindowSizeClass
import cl.duoc.mastermartiniapp.viewmodel.CategoriasUiState
import cl.duoc.mastermartiniapp.viewmodel.CategoriasViewModel

@Composable
fun CategoriasScreen(
    onCategoriaSeleccionada: (String) -> Unit,
    categoriasViewModel: CategoriasViewModel = viewModel()
) {

    val uiState by categoriasViewModel.uiState.collectAsState()

    val windowSizeClass = obtenerWindowSizeClass()

    CategoriasContenido(
        uiState = uiState,
        anchoPantalla = windowSizeClass.widthSizeClass,
        onReintentar = {
            categoriasViewModel.cargarCategorias()
        },
        onCategoriaSeleccionada = onCategoriaSeleccionada
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriasContenido(
    uiState: CategoriasUiState,
    anchoPantalla: WindowWidthSizeClass,
    onReintentar: () -> Unit,
    onCategoriaSeleccionada: (String) -> Unit
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
                    anchoPantalla = anchoPantalla,
                    onCategoriaSeleccionada = onCategoriaSeleccionada,
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
    anchoPantalla: WindowWidthSizeClass,
    onCategoriaSeleccionada: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
    ) {

        EncabezadoCategorias()

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        when (anchoPantalla) {

            WindowWidthSizeClass.Compact -> {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {

                    itemsLista(categorias) { categoria ->

                        CategoriaCard(
                            categoria = categoria,
                            onClick = {
                                onCategoriaSeleccionada(categoria.nombre)
                            }
                        )
                    }
                }
            }

            WindowWidthSizeClass.Medium -> {

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {

                    itemsGrid(categorias) { categoria ->

                        CategoriaCard(
                            categoria = categoria,
                            onClick = {
                                onCategoriaSeleccionada(categoria.nombre)
                            }
                        )
                    }
                }
            }

            else -> {

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {

                    itemsGrid(categorias) { categoria ->

                        CategoriaCard(
                            categoria = categoria,
                            onClick = {
                                onCategoriaSeleccionada(categoria.nombre)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EncabezadoCategorias() {

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
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
    }
}

@Composable
fun CategoriaCard(
    categoria: Categoria,
    onClick: () -> Unit
) {

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(12.dp),

        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),

            horizontalArrangement = Arrangement.spacedBy(16.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(76.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(10.dp)
                    ),

                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = categoria.icono,
                    fontSize = 34.sp
                )
            }

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
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Ver videos",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
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

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Cargando categorías...",
                style = MaterialTheme.typography.bodyLarge
            )
        }
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
            text = "No pudimos cargar el contenido",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )

        Text(
            text = mensaje,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
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

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                text = "Todavía no hay categorías",
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "Vuelve más tarde para descubrir nuevo contenido.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
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
                        descripcion = "Técnicas y preparaciones con chocolate",
                        icono = "🍫"
                    ),
                    Categoria(
                        nombre = "Pastelería",
                        descripcion = "Postres y técnicas profesionales",
                        icono = "🧁"
                    ),
                    Categoria(
                        nombre = "Panadería",
                        descripcion = "Masas y procesos de horneado",
                        icono = "🍞"
                    )
                )
            ),

            anchoPantalla = WindowWidthSizeClass.Compact,

            onReintentar = {},
            onCategoriaSeleccionada = {}
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

            anchoPantalla = WindowWidthSizeClass.Compact,

            onReintentar = {},
            onCategoriaSeleccionada = {}
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

            anchoPantalla = WindowWidthSizeClass.Compact,

            onReintentar = {},
            onCategoriaSeleccionada = {}
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

            anchoPantalla = WindowWidthSizeClass.Compact,

            onReintentar = {},
            onCategoriaSeleccionada = {}
        )
    }
}