package cl.duoc.mastermartiniapp.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Maneja los archivos de la foto de perfil:
 * - crea el archivo temporal donde la cámara guarda la captura (vía FileProvider),
 * - copia la imagen elegida (cámara o galería) al almacenamiento interno de la app,
 * - carga la imagen guardada para mostrarla en pantalla.
 *
 * Copiar la imagen al almacenamiento interno evita perder el acceso a fotos de la
 * galería después de reiniciar la app (el permiso temporal de la galería expira).
 */
class FotoPerfilRepository(context: Context) {

    private val appContext = context.applicationContext

    private val carpetaCamara: File
        get() = File(appContext.cacheDir, CARPETA_CAMARA).apply { mkdirs() }

    private val carpetaPerfil: File
        get() = File(appContext.filesDir, CARPETA_PERFIL).apply { mkdirs() }

    /** Crea un archivo vacío y entrega su Uri segura (content://) para la cámara. */
    fun crearUriTemporalCamara(): Uri {
        val archivo = File.createTempFile("captura_", ".jpg", carpetaCamara)
        return FileProvider.getUriForFile(
            appContext,
            "${appContext.packageName}.fileprovider",
            archivo
        )
    }

    /** Copia la imagen al almacenamiento interno y devuelve la ruta del archivo guardado. */
    suspend fun guardarComoFotoPerfil(origen: Uri): String = withContext(Dispatchers.IO) {
        val destino = File(carpetaPerfil, "foto_perfil_${System.currentTimeMillis()}.jpg")

        val entrada = appContext.contentResolver.openInputStream(origen)
            ?: throw IOException("No se pudo abrir la imagen seleccionada")

        entrada.use { input ->
            FileOutputStream(destino).use { output -> input.copyTo(output) }
        }

        if (destino.length() == 0L) {
            destino.delete()
            throw IOException("La imagen está vacía")
        }

        // Solo se conserva la foto nueva y se limpian las capturas temporales.
        carpetaPerfil.listFiles()?.filter { it != destino }?.forEach { it.delete() }
        limpiarTemporales()

        destino.absolutePath
    }

    suspend fun eliminarFotoPerfil() = withContext(Dispatchers.IO) {
        carpetaPerfil.listFiles()?.forEach { it.delete() }
        Unit
    }

    fun limpiarTemporales() {
        carpetaCamara.listFiles()?.forEach { it.delete() }
    }

    /** Lee la foto guardada, reducida a un tamaño razonable para no gastar memoria. */
    suspend fun cargarImagen(ruta: String): ImageBitmap? = withContext(Dispatchers.IO) {
        val archivo = File(ruta)
        if (!archivo.exists()) return@withContext null

        try {
            val bitmap: Bitmap? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                // ImageDecoder respeta la orientación de la foto (EXIF) automáticamente.
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(archivo)) { decoder, info, _ ->
                    val ancho = info.size.width
                    val alto = info.size.height
                    val mayor = maxOf(ancho, alto)
                    if (mayor > TAMANO_MAXIMO_PX) {
                        val escala = TAMANO_MAXIMO_PX.toFloat() / mayor
                        decoder.setTargetSize(
                            (ancho * escala).toInt().coerceAtLeast(1),
                            (alto * escala).toInt().coerceAtLeast(1)
                        )
                    }
                }
            } else {
                val limites = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                BitmapFactory.decodeFile(ruta, limites)
                var muestra = 1
                while (maxOf(limites.outWidth, limites.outHeight) / muestra > TAMANO_MAXIMO_PX) {
                    muestra *= 2
                }
                BitmapFactory.decodeFile(ruta, BitmapFactory.Options().apply { inSampleSize = muestra })
            }
            bitmap?.asImageBitmap()
        } catch (error: Exception) {
            null
        }
    }

    private companion object {
        const val CARPETA_CAMARA = "camara"
        const val CARPETA_PERFIL = "perfil"
        const val TAMANO_MAXIMO_PX = 1024
    }
}
