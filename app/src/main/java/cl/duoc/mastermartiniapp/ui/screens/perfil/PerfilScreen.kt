package cl.duoc.mastermartiniapp.ui.screens.perfil

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.mastermartiniapp.R
import cl.duoc.mastermartiniapp.model.Video
import cl.duoc.mastermartiniapp.ui.components.ImagenInteligente
import cl.duoc.mastermartiniapp.ui.screens.home.TituloSeccion
import cl.duoc.mastermartiniapp.ui.theme.MasterMartiniApp_Grupo9Theme
import cl.duoc.mastermartiniapp.viewmodel.PerfilUiState
import cl.duoc.mastermartiniapp.viewmodel.PerfilViewModel

/**
 * Pantalla "Mi perfil".
 * Aquí se integran los recursos nativos (cámara y galería), el permiso de cámara
 * y la persistencia local. La pantalla solo dibuja y avisa eventos;
 * la lógica vive en PerfilViewModel.
 */
@Composable
fun PerfilScreen(
    perfilViewModel: PerfilViewModel = viewModel()
) {
    val uiState by perfilViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    var mostrarDialogoPermiso by rememberSaveable { mutableStateOf(false) }

    // 1) Cámara: la app de cámara guarda la foto en la Uri que le entregamos.
    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { fotoTomada ->
        perfilViewModel.onResultadoCamara(fotoTomada)
    }

    // 2) Galería: el selector del sistema devuelve la Uri de la imagen (o null si se cancela).
    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        perfilViewModel.onImagenGaleria(uri)
    }

    val abrirCamara: () -> Unit = {
        val uri = perfilViewModel.prepararUriCamara()
        try {
            camaraLauncher.launch(uri)
        } catch (error: ActivityNotFoundException) {
            perfilViewModel.onCamaraNoDisponible()
        }
    }

    // 3) Permiso de cámara en tiempo de ejecución.
    val permisoCamaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            abrirCamara()
        } else {
            // Acceso negado: se explica de forma amigable y se ofrece ir a Ajustes.
            mostrarDialogoPermiso = true
        }
    }

    val solicitarCamara: () -> Unit = {
        val tienePermiso = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (tienePermiso) {
            abrirCamara()
        } else {
            permisoCamaraLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // Los mensajes del ViewModel se muestran como Snackbar.
    LaunchedEffect(uiState.mensaje) {
        uiState.mensaje?.let { mensaje ->
            snackbarHostState.showSnackbar(mensaje)
            perfilViewModel.mensajeMostrado()
        }
    }

    if (mostrarDialogoPermiso) {
        DialogoPermisoCamara(
            onAbrirAjustes = {
                mostrarDialogoPermiso = false
                abrirAjustesDeLaApp(context)
            },
            onCerrar = { mostrarDialogoPermiso = false }
        )
    }

    PerfilContenido(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onTomarFoto = solicitarCamara,
        onElegirDeGaleria = { galeriaLauncher.launch("image/*") },
        onEliminarFoto = perfilViewModel::eliminarFoto,
        onNombreChange = perfilViewModel::onNombreChange,
        onTelefonoChange = perfilViewModel::onTelefonoChange,
        onGuardar = perfilViewModel::guardarPerfil,
        onVerVideo = { video -> abrirEnlace(context, video.urlVideo) },
        onQuitarFavorito = perfilViewModel::quitarFavorito
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilContenido(
    uiState: PerfilUiState,
    snackbarHostState: SnackbarHostState,
    onTomarFoto: () -> Unit,
    onElegirDeGaleria: () -> Unit,
    onEliminarFoto: () -> Unit,
    onNombreChange: (String) -> Unit,
    onTelefonoChange: (String) -> Unit,
    onGuardar: () -> Unit,
    onVerVideo: (Video) -> Unit,
    onQuitarFavorito: (Video) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mi perfil",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->

        if (uiState.cargando) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            FormularioPerfil(
                uiState = uiState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                onTomarFoto = onTomarFoto,
                onElegirDeGaleria = onElegirDeGaleria,
                onEliminarFoto = onEliminarFoto,
                onNombreChange = onNombreChange,
                onTelefonoChange = onTelefonoChange,
                onGuardar = onGuardar,
                onVerVideo = onVerVideo,
                onQuitarFavorito = onQuitarFavorito
            )
        }
    }
}

@Composable
private fun FormularioPerfil(
    uiState: PerfilUiState,
    modifier: Modifier,
    onTomarFoto: () -> Unit,
    onElegirDeGaleria: () -> Unit,
    onEliminarFoto: () -> Unit,
    onNombreChange: (String) -> Unit,
    onTelefonoChange: (String) -> Unit,
    onGuardar: () -> Unit,
    onVerVideo: (Video) -> Unit,
    onQuitarFavorito: (Video) -> Unit
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // En tablets el contenido no se estira de lado a lado.
        Column(
            modifier = Modifier
                .widthIn(max = 560.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SeccionFoto(
                uiState = uiState,
                onTomarFoto = onTomarFoto,
                onElegirDeGaleria = onElegirDeGaleria,
                onEliminarFoto = onEliminarFoto
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outline)

            SeccionDatos(
                uiState = uiState,
                onNombreChange = onNombreChange,
                onTelefonoChange = onTelefonoChange,
                onGuardar = onGuardar
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outline)

            SeccionFavoritos(
                favoritos = uiState.favoritos,
                onVerVideo = onVerVideo,
                onQuitarFavorito = onQuitarFavorito
            )
        }
    }
}

@Composable
private fun SeccionFoto(
    uiState: PerfilUiState,
    onTomarFoto: () -> Unit,
    onElegirDeGaleria: () -> Unit,
    onEliminarFoto: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ImagenInteligente(
            imagen = uiState.foto,
            cargando = uiState.procesandoFoto
        )

        Text(
            text = if (uiState.nombre.isBlank()) "Tu perfil" else uiState.nombre,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BotonConIcono(
                texto = "Cámara",
                icono = R.drawable.ic_camara,
                habilitado = !uiState.procesandoFoto,
                onClick = onTomarFoto,
                modifier = Modifier.weight(1f)
            )
            BotonConIcono(
                texto = "Galería",
                icono = R.drawable.ic_galeria,
                habilitado = !uiState.procesandoFoto,
                onClick = onElegirDeGaleria,
                modifier = Modifier.weight(1f)
            )
        }

        AnimatedVisibility(
            visible = uiState.foto != null && !uiState.procesandoFoto,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            TextButton(onClick = onEliminarFoto) {
                Icon(
                    painter = painterResource(R.drawable.ic_eliminar),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text("Quitar foto", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
private fun BotonConIcono(
    texto: String,
    @DrawableRes icono: Int,
    habilitado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        enabled = habilitado,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp)
    ) {
        Icon(
            painter = painterResource(icono),
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(texto)
    }
}

@Composable
private fun SeccionDatos(
    uiState: PerfilUiState,
    onNombreChange: (String) -> Unit,
    onTelefonoChange: (String) -> Unit,
    onGuardar: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TituloSeccion(texto = "Mis datos")

        CampoPerfil(
            valor = uiState.nombre,
            etiqueta = "Nombre",
            ayuda = "Como quieres que te llamemos",
            error = uiState.nombreError,
            icono = R.drawable.ic_persona,
            tipoTeclado = KeyboardType.Text,
            onValorChange = onNombreChange
        )

        CampoPerfil(
            valor = uiState.telefono,
            etiqueta = "Celular",
            ayuda = "Ejemplo: 9 1234 5678",
            error = uiState.telefonoError,
            icono = R.drawable.ic_telefono,
            tipoTeclado = KeyboardType.Phone,
            onValorChange = onTelefonoChange
        )

        Button(
            onClick = onGuardar,
            enabled = !uiState.guardando,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            if (uiState.guardando) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(Modifier.width(10.dp))
                Text("Guardando...")
            } else {
                Icon(
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Guardar perfil")
            }
        }

        AnimatedVisibility(
            visible = uiState.guardadoExitoso,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
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
                        text = "Perfil guardado en este dispositivo.",
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun CampoPerfil(
    valor: String,
    etiqueta: String,
    ayuda: String,
    error: String?,
    @DrawableRes icono: Int,
    tipoTeclado: KeyboardType,
    onValorChange: (String) -> Unit
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValorChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(etiqueta) },
        singleLine = true,
        isError = error != null,
        leadingIcon = {
            Icon(painter = painterResource(icono), contentDescription = null)
        },
        trailingIcon = if (error != null) {
            {
                Icon(
                    painter = painterResource(R.drawable.ic_error),
                    contentDescription = "Error",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        } else {
            null
        },
        supportingText = { Text(error ?: ayuda) },
        keyboardOptions = KeyboardOptions(
            keyboardType = tipoTeclado,
            imeAction = ImeAction.Next
        ),
        shape = RoundedCornerShape(8.dp)
    )
}

@Composable
private fun SeccionFavoritos(
    favoritos: List<Video>,
    onVerVideo: (Video) -> Unit,
    onQuitarFavorito: (Video) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TituloSeccion(texto = "Mis videos favoritos")

        if (favoritos.isEmpty()) {
            Text(
                text = "Aún no tienes favoritos. Toca el corazón de cualquier video para guardarlo aquí.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            favoritos.forEach { video ->
                key(video.id) {
                    FavoritoItem(
                        video = video,
                        onVer = { onVerVideo(video) },
                        onQuitar = { onQuitarFavorito(video) }
                    )
                }
            }
        }
    }
}

@Composable
private fun FavoritoItem(
    video: Video,
    onVer: () -> Unit,
    onQuitar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = video.categoria.uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = video.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(onClick = onVer) {
                Icon(
                    painter = painterResource(R.drawable.ic_reproducir),
                    contentDescription = "Ver ${video.titulo}",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = onQuitar) {
                Icon(
                    painter = painterResource(R.drawable.ic_eliminar),
                    contentDescription = "Quitar ${video.titulo} de favoritos",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun DialogoPermisoCamara(
    onAbrirAjustes: () -> Unit,
    onCerrar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCerrar,
        icon = {
            Icon(painter = painterResource(R.drawable.ic_camara), contentDescription = null)
        },
        title = { Text("Permiso de cámara") },
        text = {
            Text(
                "Para tomar tu foto de perfil necesitamos acceso a la cámara. " +
                    "Puedes activarlo en los ajustes de la aplicación o elegir una foto desde la galería."
            )
        },
        confirmButton = {
            TextButton(onClick = onAbrirAjustes) { Text("Abrir ajustes") }
        },
        dismissButton = {
            TextButton(onClick = onCerrar) { Text("Ahora no") }
        }
    )
}

private fun abrirAjustesDeLaApp(context: Context) {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", context.packageName, null)
    )
    try {
        context.startActivity(intent)
    } catch (error: ActivityNotFoundException) {
        // Algunos equipos no tienen esta pantalla; simplemente no se abre.
    }
}

private fun abrirEnlace(context: Context, url: String) {
    try {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    } catch (error: ActivityNotFoundException) {
        // No hay navegador ni app de YouTube: no se abre nada.
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun PerfilPreview() {
    MasterMartiniApp_Grupo9Theme {
        PerfilContenido(
            uiState = PerfilUiState(
                cargando = false,
                nombre = "Eduardo",
                telefono = "9 1234 56",
                telefonoError = "Celular no válido. Ejemplo: 9 1234 5678",
                favoritos = listOf(
                    Video(
                        id = 1,
                        titulo = "Técnica con chocolate",
                        descripcion = "",
                        categoria = "Chocolatería",
                        urlVideo = ""
                    )
                )
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onTomarFoto = {},
            onElegirDeGaleria = {},
            onEliminarFoto = {},
            onNombreChange = {},
            onTelefonoChange = {},
            onGuardar = {},
            onVerVideo = {},
            onQuitarFavorito = {}
        )
    }
}
