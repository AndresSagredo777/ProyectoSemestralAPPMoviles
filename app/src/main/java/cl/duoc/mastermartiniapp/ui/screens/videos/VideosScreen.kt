package cl.duoc.mastermartiniapp.ui.screens.videos

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import cl.duoc.mastermartiniapp.model.Video
import cl.duoc.mastermartiniapp.viewmodel.VideosUiState
import cl.duoc.mastermartiniapp.viewmodel.VideosViewModel

@Composable
fun VideosScreen(
    categoria: String,
    videosViewModel: VideosViewModel = viewModel()
) {
    val uiState by videosViewModel.uiState.collectAsState()
    val context = LocalContext.current

    VideosContenido(
        categoria = categoria,
        uiState = uiState,
        onReintentar = videosViewModel::cargarVideos,
        onVerVideo = { url ->
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        },
        onAlternarFavorito = videosViewModel::alternarFavorito
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideosContenido(
    categoria: String,
    uiState: VideosUiState,
    onReintentar: () -> Unit,
    onVerVideo: (String) -> Unit,
    onAlternarFavorito: (Long) -> Unit = {}
) {
    val videosCategoria = uiState.videos.filter { video ->
        video.categoria.equals(categoria, ignoreCase = true)
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
                }
            )
        }
    ) { innerPadding ->
        when {
            uiState.cargando -> EstadoCargandoVideos(
                modifier = Modifier.fillMaxSize().padding(innerPadding)
            )
            uiState.mensajeError != null -> EstadoErrorVideos(
                mensaje = uiState.mensajeError,
                onReintentar = onReintentar,
                modifier = Modifier.fillMaxSize().padding(innerPadding)
            )
            videosCategoria.isEmpty() -> EstadoVacioVideos(
                categoria = categoria,
                modifier = Modifier.fillMaxSize().padding(innerPadding)
            )
            else -> ListaVideos(
                videos = videosCategoria,
                favoritosIds = uiState.favoritosIds,
                onVerVideo = onVerVideo,
                onAlternarFavorito = onAlternarFavorito,
                modifier = Modifier.fillMaxSize().padding(innerPadding)
            )
        }
    }
}

@Composable
fun ListaVideos(
    videos: List<Video>,
    favoritosIds: Set<Long>,
    onVerVideo: (String) -> Unit,
    onAlternarFavorito: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(videos, key = { it.id }) { video ->
            VideoCard(
                video = video,
                esFavorito = video.id in favoritosIds,
                onVerVideo = { onVerVideo(video.urlVideo) },
                onAlternarFavorito = { onAlternarFavorito(video.id) }
            )
        }
    }
}

@Composable
fun VideoCard(
    video: Video,
    esFavorito: Boolean,
    onVerVideo: () -> Unit,
    onAlternarFavorito: () -> Unit
) {
    val miniatura = remember(video.urlVideo) { obtenerMiniaturaYoutube(video.urlVideo) }
    var errorMiniatura by remember(video.urlVideo) { mutableStateOf(miniatura == null) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
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

            Text(
                text = video.categoria.uppercase(),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium
            )
            Text(video.titulo, style = MaterialTheme.typography.titleLarge)
            Text(video.descripcion, style = MaterialTheme.typography.bodyMedium)

            Button(
                onClick = onVerVideo,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("▶  Ver video")
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = {},
                    enabled = false,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("PDF no disponible")
                }
                OutlinedButton(
                    onClick = onAlternarFavorito,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(if (esFavorito) "★ Favorito" else "☆ Favorito")
                }
            }
        }
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
fun EstadoCargandoVideos(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
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
            "No pudimos cargar los videos",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
        Text(mensaje, modifier = Modifier.padding(top = 8.dp), textAlign = TextAlign.Center)
        Button(onClick = onReintentar, modifier = Modifier.padding(top = 16.dp)) {
            Text("Reintentar")
        }
    }
}

@Composable
fun EstadoVacioVideos(categoria: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier.padding(24.dp), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Sin videos disponibles", style = MaterialTheme.typography.titleLarge)
            Text(
                "Todavía no hay videos para $categoria.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}
