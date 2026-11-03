# Master Martini — Aplicación móvil

Proyecto académico de **Desarrollo de Aplicaciones Móviles (DSY1105), Duoc UC**. Aplicación Android orientada a explorar contenido educativo de Master Martini, organizado en categorías y videos de especialidades como chocolatería, pastelería y heladería.

> **Proyecto académico / prototipo funcional.** Los datos de categorías y videos se entregan desde listas definidas en el backend; no hay una base de datos de servidor configurada.

## Integrantes

- **Agustín:** Agustín Pérez — Home adaptable, categorías, videos, navegación, integración Retrofit y backend básico.
- **Eduardo:** Eduardo Caro — perfil, recursos nativos, almacenamiento local, favoritos y navegación principal.
- **Andrés:** Andrés Sagredo — formulario de contacto, validaciones y guardado local del borrador.

**Grupo:** Grupo 9

## Funcionalidades

- Interfaz con **Jetpack Compose y Material 3**, siguiendo una identidad visual compartida.
- **Home adaptable** a tamaños de ventana Compact, Medium y Expanded.
- **Navegación** entre Inicio, Categorías, Videos y Mi perfil.
- **Categorías** obtenidas desde el backend, con estados de carga, error y ausencia de contenido.
- **Videos** obtenidos desde API REST, filtrados por categoría y abiertos mediante `Intent` en YouTube o navegador.
- **Perfil local:** formulario de nombre y celular con validaciones y mensajes por campo.
- **Cámara y galería** para elegir la foto de perfil, con manejo de permisos y almacenamiento de la imagen dentro de la app.
- **Favoritos de videos**, almacenados localmente para conservar la selección al cerrar y volver a abrir la aplicación.
- **Persistencia local** mediante DataStore (perfil y favoritos).
- **Animaciones funcionales** de confirmación, foto de perfil y botón de favoritos.
- **Formulario de contacto** con nombre, correo electrónico y mensaje, validación visual y guardado local mediante SharedPreferences. **No envía correos ni solicitudes a un servidor**: conserva un borrador en el dispositivo.

## Tecnologías utilizadas

| Componente               | Tecnología                                                   |
| ------------------------ | ------------------------------------------------------------ |
| App móvil                | Kotlin, Android, Jetpack Compose, Material 3                 |
| Navegación               | Navigation Compose                                           |
| Estado de pantallas      | ViewModel, UiState, StateFlow y corrutinas                   |
| Cliente HTTP             | Retrofit + Gson                                              |
| Persistencia local       | DataStore Preferences y SharedPreferences                    |
| Recursos del dispositivo | Cámara (`TakePicture`), galería (`GetContent`), FileProvider |
| Backend                  | Java 17, Spring Boot, API REST                               |
| Colaboración             | Git, GitHub y Trello                                         |

## Estructura del proyecto

```text
app/
  src/main/java/cl/duoc/mastermartiniapp/
    navigation/      # Rutas y grafo de navegación
    ui/screens/      # Home, categorías, videos, perfil y formulario
    ui/theme/        # Colores, tipografía y tema visual
    viewmodel/       # Lógica, validaciones y estado de las pantallas
    repository/      # Acceso a API y almacenamiento local
    network/         # Configuración de Retrofit y operaciones HTTP
    data/local/      # Configuración de DataStore
    model/           # Modelos de datos Kotlin
backend/
  src/main/java/cl/duoc/mastermartini/
    controller/      # Endpoints REST de categorías y videos
    model/           # Modelos de datos Java
```

## Requisitos para ejecutar

- Android Studio con el SDK requerido por el proyecto, un emulador Android o teléfono de prueba.
- JDK **17** para el backend Spring Boot.
- Conexión a Internet para obtener dependencias y abrir los videos de YouTube.
- Gradle se administra mediante el _wrapper_ incluido en el repositorio.

## Instrucciones de ejecución

### 1. Iniciar el backend

En Windows, desde una terminal ubicada en la carpeta raíz del repositorio:

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

La API queda disponible en el puerto **8080**. Comprobar en el navegador del computador:

- `http://localhost:8080/api/categorias`
- `http://localhost:8080/api/videos`

Ambos endpoints deben devolver datos JSON. Mantener el backend ejecutándose mientras se prueba la aplicación.

### 2. Ejecutar la app Android

1. Abrir **Android Studio** y seleccionar la **carpeta raíz del repositorio** (la que contiene `settings.gradle.kts`).
2. Esperar la sincronización de Gradle; comprobar que no haya errores de compilación.
3. Seleccionar un emulador Android compatible (o un dispositivo físico configurado para depuración).
4. Ejecutar la configuración de la aplicación (`app`) con **Run**.
5. Recorrer Inicio → Categorías → Videos; luego probar Mi perfil y el formulario de contacto.

La dirección configurada en `ClienteRetrofit.kt` es:

```kotlin
private const val BASE_URL = "http://10.0.2.2:8080/"
```

**`10.0.2.2` se utiliza desde el emulador Android** para acceder al servidor que está en el mismo computador. Si se usa un teléfono físico, se debe configurar la IP local del computador como `BASE_URL`, conectar ambos a la misma red y permitir el acceso al puerto 8080.

## Pruebas realizadas

Se verificó la integración de los tres módulos del proyecto mediante compilación, pruebas unitarias y ejecución en un emulador Android.

- Compilación exitosa mediante `:app:assembleDebug`.
- Ejecución exitosa de la tarea `:app:testDebugUnitTest`.
- Inicio correcto de la aplicación y navegación entre pantallas.
- Consulta y visualización de categorías y videos desde el backend Spring Boot.
- Registro y persistencia de datos del perfil después de cerrar y volver a abrir la aplicación.
- Captura de fotografías con la cámara y actualización de la foto de perfil.
- Visualización de videos favoritos.

**Resultado:** La aplicación compiló correctamente y las funcionalidades indicadas se comprobaron en el emulador.

## Consideraciones

- Los endpoints del backend devuelven datos de ejemplo en memoria y no utilizan una base de datos persistente.
- El filtrado de videos por categoría se realiza en Android sobre la respuesta de `/api/videos`.
- El almacenamiento del perfil y favoritos **es local al dispositivo**; no se sincroniza con el backend.
- El formulario de contacto **no implementa un envío real de consulta**. El botón valida y conserva el borrador local.
- El código debe quedar congelado tras la entrega y antes de la evaluación, según las instrucciones de EP2.
