# Guía para empezar: Master Martini

Esta guía explica el proyecto con palabras sencillas y señala dónde buscar cuando tengas que hacer cambios.

## ¿Qué es este proyecto?

Es una aplicación Android de contenido educativo sobre pastelería, panadería y otras especialidades. Tiene dos partes:

- **App Android** (`app/`): lo que se instala y se ve en el teléfono o emulador. Está escrita en Kotlin y usa Jetpack Compose para dibujar las pantallas.
- **Servidor** (`backend/`): un programa Java con Spring Boot que entrega las categorías y los videos a la app mediante una API web.

El contenido actual está escrito directamente en el código del servidor; no se observa una base de datos configurada.

## Cómo se usa

1. Al abrir la app, aparece la portada.
2. Al pulsar **Explorar**, se abre la lista de categorías.
3. La app pide las categorías al servidor y muestra un indicador mientras espera. Si hay un problema, ofrece volver a intentar.
4. Al elegir una categoría, se abre la pantalla de videos de esa categoría.
5. Al pulsar **Ver video**, Android abre el enlace con la aplicación disponible, normalmente YouTube o un navegador.

La pantalla de inicio adapta su diseño a distintos tamaños de pantalla. La lista de categorías usa una columna en móviles y más columnas en pantallas mayores.

## Cómo se conectan las piezas

```text
Pantalla (Compose)
   ↓ observa
ViewModel (estado y carga)
   ↓ solicita datos
Repository (intermediario)
   ↓ llama a
Retrofit / ApiService (petición HTTP)
   ↓
Servidor Spring Boot (backend)
   ↓ responde JSON
La app convierte la respuesta a modelos Kotlin y actualiza la pantalla
```

**En simple:** la pantalla muestra; el ViewModel coordina; el repositorio obtiene los datos; Retrofit habla por internet; el servidor responde.

## Archivos que conviene conocer

### App Android: `app/src/main/java/cl/duoc/mastermartiniapp/`

- `MainActivity.kt`: punto de entrada de Android. Aplica el tema y muestra la navegación.
- `navigation/NavigationApp.kt` y `navigation/Rutas.kt`: definen las pantallas y el paso de inicio a categorías y videos.
- `ui/screens/home/`: portada y sus variantes para tamaños de ventana.
- `ui/screens/categories/CategoriesScreen.kt`: lista y tarjetas de categorías, estados de carga, error y lista vacía.
- `ui/screens/videos/VideosScreen.kt`: lista de videos, filtro por categoría y apertura del enlace.
- `viewmodel/`: carga datos y conserva el estado que observa la pantalla.
- `repository/`: intermediarios para obtener categorías y videos.
- `network/ApiService.kt`: rutas HTTP que entiende la app.
- `network/ClienteRetrofit.kt`: dirección del servidor y configuración de Retrofit.
- `model/`: forma de los datos de categoría y video que usa la app.
- `ui/theme/`: colores, tipografías y tema visual.
- `ui/utils/WindowSizeUtils.kt`: detección del tamaño disponible de pantalla.
- `src/main/res/`: imágenes, fuentes, textos y recursos de Android.
- `src/main/AndroidManifest.xml`: declara permisos y la actividad de inicio. El permiso de internet permite llamar al backend.

### Servidor: `backend/src/main/java/cl/duoc/mastermartini/`

- `MastermartiniBackendApplication.java`: arranca Spring Boot.
- `controller/CategoriaController.java`: responde a `GET /api/categorias`.
- `controller/VideoController.java`: responde a `GET /api/videos`.
- `model/Categoria.java` y `model/Video.java`: estructura de cada objeto enviado como JSON.
- `src/main/resources/application.properties`: configuración del servidor; por ahora solo define su nombre.

## Cómo abrir y ejecutar

### Abrir

1. Abre Android Studio.
2. Elige **Open** y selecciona la carpeta `ProyectoSemestralAPPMoviles`.
3. Espera a que termine la sincronización de Gradle.
4. Inicia un emulador Android desde Device Manager o conecta un teléfono con depuración USB.

### Iniciar el servidor

La app apunta a `http://10.0.2.2:8080/`. Esa dirección especial permite que el emulador Android alcance el puerto 8080 del mismo computador.

Desde Android Studio abre la carpeta `backend` como proyecto Maven y ejecuta la clase `MastermartiniBackendApplication`, o desde una terminal ubicada en la carpeta `backend` ejecuta:

```powershell
.\mvnw.cmd spring-boot:run
```

El backend requiere Java 17 según `backend/pom.xml`. Puedes comprobarlo con `java -version`. Deja el servidor ejecutándose.

### Iniciar la app

En Android Studio selecciona el emulador y pulsa el botón verde **Run**. El servidor debe estar en marcha para cargar categorías y videos.

Para revisar el servidor en el navegador del computador, abre `http://localhost:8080/api/categorias` o `http://localhost:8080/api/videos`. Deberías ver datos en formato JSON.

> La dirección `10.0.2.2` es para el emulador Android. En un teléfono físico, normalmente debes cambiar `BASE_URL` en `ClienteRetrofit.kt` por la IP local del computador, usar el mismo Wi-Fi y permitir el acceso del firewall al puerto 8080.

## Dónde hacer cambios comunes

- **Cambiar colores o letras:** `ui/theme/Color.kt` y `ui/theme/Type.kt`.
- **Cambiar textos o diseño de una pantalla:** el archivo de esa pantalla dentro de `ui/screens/`.
- **Agregar o cambiar categorías/videos:** los datos están actualmente en `CategoriaController.java` y `VideoController.java`.
- **Cambiar la dirección del servidor:** `network/ClienteRetrofit.kt`.
- **Agregar una nueva pantalla:** crear el Composable correspondiente y registrar su ruta en `navigation/`.
- **Cambiar el comportamiento de carga:** revisar el ViewModel y el Repository asociados.

## Cosas a tener presentes

- La API entrega los datos desde listas escritas en los controladores; reiniciar el servidor restaura esos datos tal como están en el código.
- La app descarga la lista completa de videos y filtra por categoría en el teléfono. Para el tamaño actual es sencillo; si el catálogo crece, convendría que el servidor filtrara por categoría.
- Si no aparecen datos, comprueba primero que el backend esté activo en el puerto 8080 y que la URL corresponda al tipo de dispositivo.
- En `app/build.gradle.kts`, el `minSdk` es 24, por lo que el proyecto declara compatibilidad con Android 7.0 o superior.
- El proyecto tiene pruebas de ejemplo, pero esta guía no las ejecuta.

## Vocabulario mínimo

- **Pantalla / Composable:** parte visual de la aplicación.
- **API:** conjunto de direcciones que permiten a la app pedir datos al servidor.
- **Endpoint:** una dirección específica de la API, como `/api/categorias`.
- **JSON:** formato de texto usado para enviar objetos y listas por la red.
- **ViewModel:** pieza que carga y mantiene los datos que una pantalla necesita.
- **Repositorio:** intermediario que organiza el acceso a los datos.
- **Backend:** programa que corre en el computador o servidor y responde a la app.
- **Emulador:** teléfono Android simulado dentro del computador.
