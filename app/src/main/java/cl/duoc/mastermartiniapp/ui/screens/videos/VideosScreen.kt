package cl.duoc.mastermartiniapp.ui.screens.videos

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.app.DownloadManager
import android.os.Environment
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import cl.duoc.mastermartiniapp.R
import cl.duoc.mastermartiniapp.model.Video
import cl.duoc.mastermartiniapp.network.ClienteRetrofit
import cl.duoc.mastermartiniapp.viewmodel.FavoritosViewModel
import cl.duoc.mastermartiniapp.viewmodel.VideosUiState
import cl.duoc.mastermartiniapp.viewmodel.VideosViewModel

@Composable
fun VideosScreen(
    categoria: String,
    onVolver: (() -> Unit)? = null,
    videosViewModel: VideosViewModel = viewModel(),
    favoritosViewModel: FavoritosViewModel = viewModel()
) {

    val uiState by videosViewModel.uiState.collectAsState()

    val idsFavoritos by favoritosViewModel.idsFavoritos.collectAsState()

    val context = LocalContext.current

    VideosContenido(
        categoria = categoria,
        uiState = uiState,
        idsFavoritos = idsFavoritos,
        onVolver = onVolver,

        onReintentar = {
            videosViewModel.cargarVideos()
        },

        onVerVideo = { url ->

            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url)
            )

            try {
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                // No hay app para abrir el enlace (YouTube o navegador).
            }
        },

        onAlternarFavorito = { video ->
            favoritosViewModel.alternarFavorito(video)
        },
        onDescargarReceta = { url, nombre ->
            val request = DownloadManager.Request(
                Uri.parse(ClienteRetrofit.resolverUrl(url))
            )
                .setTitle("Ingredientes: $nombre")
                .setDescription("Descargando ingredientes en PDF")
                .setMimeType("application/pdf")
                .setNotificationVisibility(
                    DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
                )
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, nombre)

            context.getSystemService(DownloadManager::class.java).enqueue(request)
            Toast.makeText(
                context,
                "Descarga iniciada. Revisa las notificaciones.",
                Toast.LENGTH_SHORT
            ).show()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideosContenido(
    categoria: String,
    uiState: VideosUiState,
    onReintentar: () -> Unit,
    onVerVideo: (String) -> Unit,
    idsFavoritos: Set<Long> = emptySet(),
    onAlternarFavorito: (Video) -> Unit = {},
    onVolver: (() -> Unit)? = null,
    onDescargarReceta: (String, String) -> Unit = { _, _ -> }
) {

    val videosCategoria =
        uiState.videos.filter { video ->
            video.categoria.equals(
                categoria,
                ignoreCase = true
            )
        }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,

        topBar = {

            TopAppBar(
                title = {

                    Text(
                        text = "Videos · $categoria",
                        style = MaterialTheme.typography.titleLarge
                    )
                },

                navigationIcon = {

                    if (onVolver != null) {

                        IconButton(onClick = onVolver) {

                            Icon(
                                painter = painterResource(R.drawable.ic_volver),
                                contentDescription = "Volver a categorías"
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->

        when {

            uiState.cargando -> {

                EstadoCargandoVideos(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }

            uiState.mensajeError != null -> {

                EstadoErrorVideos(
                    mensaje = uiState.mensajeError,
                    onReintentar = onReintentar,

                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }

            videosCategoria.isEmpty() -> {

                EstadoVacioVideos(
                    categoria = categoria,

                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }

            else -> {

                ListaVideos(
                    videos = videosCategoria,
                    idsFavoritos = idsFavoritos,
                    onVerVideo = onVerVideo,
                    onAlternarFavorito = onAlternarFavorito,
                    onDescargarReceta = onDescargarReceta,

                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }
        }
    }
}

@Composable
fun ListaVideos(
    videos: List<Video>,
    onVerVideo: (String) -> Unit,
    modifier: Modifier = Modifier,
    idsFavoritos: Set<Long> = emptySet(),
    onAlternarFavorito: (Video) -> Unit = {},
    onDescargarReceta: (String, String) -> Unit = { _, _ -> }
) {

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        items(videos) { video ->

            VideoCard(
                video = video,
                esFavorito = video.id in idsFavoritos,
                onVerVideo = {
                    onVerVideo(video.urlVideo)
                },
                onAlternarFavorito = {
                    onAlternarFavorito(video)
                },
                onDescargarReceta = {
                    video.urlRecetaPdf?.let { url ->
                        onDescargarReceta(url, Uri.parse(url).lastPathSegment ?: "ingredientes.pdf")
                    }
                }
            )
        }
    }
}

@Composable
fun VideoCard(
    video: Video,
    onVerVideo: () -> Unit,
    esFavorito: Boolean = false,
    onAlternarFavorito: () -> Unit = {},
    onDescargarReceta: () -> Unit = {}
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),

        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            val miniatura = remember(video.urlVideo) { obtenerMiniaturaYoutube(video.urlVideo) }
            var errorMiniatura by remember(video.urlVideo) { mutableStateOf(miniatura == null) }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                if (!errorMiniatura && miniatura != null) {
                    AsyncImage(
                        model = miniatura,
                        contentDescription = "Miniatura del video ${video.titulo}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        onError = { errorMiniatura = true }
                    )
                }
                if (errorMiniatura) {
                    Text("Miniatura no disponible", color = MaterialTheme.colorScheme.onPrimaryContainer)
                } else {
                    Text(
                        text = "▶",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = 0.65f), CircleShape)
                            .padding(horizontal = 18.dp, vertical = 10.dp)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = video.categoria.uppercase(),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.weight(1f)
                )

                BotonFavorito(
                    esFavorito = esFavorito,
                    tituloVideo = video.titulo,
                    onClick = onAlternarFavorito
                )
            }

            Text(
                text = video.titulo,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = video.descripcion,
                style = MaterialTheme.typography.bodyMedium
            )

            obtenerRecetaVideo(video.id)?.let { receta ->
                IngredientesVideo(receta = receta)
            }

            Button(
                onClick = onVerVideo,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {

                Text("Ver video")
            }

            OutlinedButton(
                onClick = onDescargarReceta,
                enabled = video.urlRecetaPdf != null,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(if (video.urlRecetaPdf != null) "Descargar ingredientes (PDF)" else "PDF no disponible")
            }
        }
    }
}

/**
 * Corazón para guardar el video en favoritos (persistencia local).
 * Anima su color y hace un pequeño "rebote" al marcarse.
 */
@Composable
fun BotonFavorito(
    esFavorito: Boolean,
    tituloVideo: String,
    onClick: () -> Unit
) {

    val color by animateColorAsState(
        targetValue = if (esFavorito) {
            MaterialTheme.colorScheme.error
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        label = "colorFavorito"
    )

    val escala by animateFloatAsState(
        targetValue = if (esFavorito) 1.2f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "escalaFavorito"
    )

    IconButton(onClick = onClick) {

        Icon(
            painter = painterResource(
                if (esFavorito) R.drawable.ic_favorito else R.drawable.ic_favorito_borde
            ),
            contentDescription = if (esFavorito) {
                "Quitar $tituloVideo de favoritos"
            } else {
                "Agregar $tituloVideo a favoritos"
            },
            tint = color,
            modifier = Modifier.scale(escala)
        )
    }
}

private fun obtenerMiniaturaYoutube(url: String): String? {
    val uri = Uri.parse(url)
    val id = when {
        uri.host?.contains("youtu.be", ignoreCase = true) == true -> uri.lastPathSegment
        uri.host?.contains("youtube.com", ignoreCase = true) == true -> uri.getQueryParameter("v")
        else -> null
    }?.takeIf { it.isNotBlank() } ?: return null

    return "https://img.youtube.com/vi/$id/hqdefault.jpg"
}

@Composable
fun EstadoCargandoVideos(
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

            Text("Cargando videos...")
        }
    }
}

@Composable
fun EstadoErrorVideos(
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
            text = "No pudimos cargar los videos",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )

        Text(
            text = mensaje,
            modifier = Modifier.padding(top = 8.dp),
            textAlign = TextAlign.Center
        )

        Button(
            onClick = onReintentar,
            modifier = Modifier.padding(top = 16.dp)
        ) {

            Text("Reintentar")
        }
    }
}

@Composable
fun EstadoVacioVideos(
    categoria: String,
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
                text = "Sin videos disponibles",
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "Todavía no hay videos para $categoria.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}
