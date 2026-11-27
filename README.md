# Master Martini — Aplicación móvil

Proyecto académico de **Desarrollo de Aplicaciones Móviles (DSY1105), Duoc UC**, desarrollado por el Grupo 9. La aplicación Android permite explorar contenido de Master Martini organizado en especialidades y videos, acceder a recetas en PDF y utilizar funciones locales como favoritos, perfil y formulario de contacto.

## Integrantes

- **Agustín Pérez:** Home adaptable, categorías, videos, navegación, integración Retrofit y backend Spring Boot con persistencia H2.
- **Eduardo Caro:** perfil, recursos nativos, almacenamiento local, favoritos y navegación principal.
- **Andrés Sagredo:** formulario de contacto, validaciones y guardado local del borrador.

## Funcionalidades

- Interfaz Android con **Kotlin, Jetpack Compose y Material 3**.
- Home adaptable a tamaños de ventana compactos, medianos y expandidos.
- Navegación por Inicio, Categorías, Videos y Perfil.
- **6 especialidades** obtenidas desde una API REST: Chocolatería, Pastelería, Panadería, Heladería, Bollería y Galletas y horneo.
- **4 videos iniciales**, consultados desde la API, filtrados por especialidad y abiertos con YouTube o el navegador.
- Descarga de archivos PDF de recetas desde el backend.
- Perfil con validaciones, selección de foto mediante cámara o galería y almacenamiento local.
- Favoritos de videos y datos de perfil persistidos localmente con DataStore.
- Formulario de contacto con validaciones y borrador local mediante SharedPreferences; **no envía mensajes a un servidor**.
- Pantallas y funciones para los roles contemplados en el proyecto (administrador, chef y usuario).

## Tecnologías

| Capa | Tecnologías |
| --- | --- |
| Android | Kotlin, Jetpack Compose, Material 3, Navigation Compose |
| Arquitectura Android | ViewModel, UiState, StateFlow, corrutinas |
| Comunicación | Retrofit y Gson; HTTP/JSON |
| Backend | Java 17, Spring Boot 4.1.1, Spring Web MVC |
| Persistencia del servidor | Spring Data JPA, Hibernate, H2 en archivo local |
| Persistencia del dispositivo | DataStore Preferences y SharedPreferences |
| Recursos Android | Cámara, galería, FileProvider, DownloadManager |
| Herramientas | Android Studio, Maven Wrapper, Gradle Wrapper, Git y GitHub |

## Arquitectura

Flujo de consulta de especialidades y videos:

```text
Android (Jetpack Compose)
  -> ViewModel
  -> Repository de Android / Retrofit
  -> API REST (Controller de Spring Boot)
  -> Service
  -> Repository de Spring Data JPA
  -> Base de datos H2
```

El backend está organizado en los paquetes `controller`, `service`, `repository`, `model` y `config`.

- **Controller:** recibe las solicitudes HTTP y devuelve JSON.
- **Service:** coordina las operaciones sobre las entidades.
- **Repository:** utiliza Spring Data JPA para acceder a H2.
- **Model:** define las entidades `Categoria` y `Video`.
- **Config:** incorpora los datos iniciales si las tablas correspondientes están vacías.

H2 utiliza la configuración `jdbc:h2:file:./data/mastermartini` en `backend/src/main/resources/application.properties`. Si el backend se ejecuta desde la carpeta `backend`, la base se guarda en `backend/data/`. Esta carpeta está excluida de Git: **cada integrante genera su propia base local** al iniciar el proyecto.

La primera ejecución crea las tablas e incorpora **6 categorías y 4 videos**. La base de datos conserva los cambios después de reiniciar el servidor.

## Estructura del repositorio

```text
app/
  src/main/java/cl/duoc/mastermartiniapp/
    navigation/    # Navegación
    ui/screens/    # Pantallas de la aplicación
    viewmodel/     # Estado y lógica de presentación
    repository/    # API y almacenamiento local
    network/       # Retrofit y endpoints
    data/local/    # DataStore
    model/         # Modelos Android
backend/
  src/main/java/cl/duoc/mastermartini/
    controller/    # Endpoints REST
    service/       # Servicios
    repository/    # Spring Data JPA
    model/         # Entidades JPA
    config/        # Datos iniciales
  src/main/resources/
    application.properties
    static/recetas/ # PDF disponibles desde el backend
  src/test/
    java/          # Tests de Spring Boot
    resources/     # Configuración H2 temporal para pruebas
```

## Requisitos

- Android Studio y SDK compatible con la configuración Gradle del proyecto.
- Emulador de Android o teléfono compatible para ejecutar la aplicación.
- JDK 17 o superior compatible con Spring Boot 4.1.1.
- Conexión a Internet para obtener dependencias y abrir videos externos.
- Puerto `8080` disponible para el backend.

Se incluyen los wrappers de Maven y Gradle, por lo que no es necesario instalar manualmente ambos programas.

## Ejecución local

### 1. Iniciar el backend

Abrir PowerShell desde la raíz del repositorio y ejecutar:

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

Mantener esa terminal abierta mientras se utiliza Android. Cuando el servidor esté iniciado, comprobar:

- `http://localhost:8080/api/categorias`
- `http://localhost:8080/api/videos`

En una instalación nueva, la API debe mostrar 6 categorías y 4 videos iniciales.

**Importante:** no iniciar dos servidores que usen simultáneamente el mismo archivo H2, porque puede aparecer un error de base de datos bloqueada.

### 2. Configurar Retrofit

La URL se define en:

`app/src/main/java/cl/duoc/mastermartiniapp/network/ClienteRetrofit.kt`

Para el **emulador Android Studio**, con el backend ejecutándose en el mismo computador:

```kotlin
private const val BASE_URL = "http://10.0.2.2:8080/"
```

`10.0.2.2` permite que el emulador se conecte al `localhost` del computador anfitrión.

Para un **celular físico**, cambiar esa URL por la dirección IP local del computador que ejecuta el backend, verificar que estén en la misma red y permitir las conexiones al puerto `8080`.

### 3. Ejecutar Android

1. Abrir en Android Studio la **carpeta raíz** del repositorio, donde está `settings.gradle.kts`.
2. Esperar la sincronización de Gradle.
3. Iniciar el backend y confirmar sus endpoints.
4. Seleccionar un emulador o un dispositivo Android conectado.
5. Ejecutar el módulo `app` y recorrer Inicio, Categorías, Videos y Perfil.

### 4. Ejecutar los tests del backend

Desde `backend`:

```powershell
.\mvnw.cmd test
```

Los tests usan una base H2 **en memoria** configurada en `src/test/resources/application.properties`, separada de la base de datos del servidor. El test actual comprueba que el contexto de Spring Boot se inicia correctamente.

## API REST

| Método | Ruta | Descripción |
| --- | --- | --- |
| GET | `/api/categorias` | Obtener todas las categorías de H2. |
| GET | `/api/videos` | Obtener todos los videos de H2. |
| POST | `/api/videos` | Guardar un video recibido como JSON. |
| DELETE | `/api/videos/{id}` | Eliminar un video por su ID; responde `204` si se elimina o `404` si no existe. |

Los endpoints POST y DELETE están destinados a las pruebas académicas locales. **No cuentan con autenticación ni validaciones completas**, por lo que el backend no está preparado para publicarse como servicio abierto en Internet.

## Comprobaciones realizadas

- Compilación e inicio del backend Spring Boot con conexión a H2.
- Creación de tablas `categorias` y `videos` por JPA.
- Consultas GET con resultado de **6 categorías y 4 videos**.
- Registro de un quinto video de prueba mediante POST.
- Confirmación de ese video dentro de la aplicación Android.
- Verificación de persistencia tras reiniciar Spring Boot.
- Eliminación del video de prueba mediante DELETE; se restauró la cantidad original de 4 videos.
- Ejecución de `mvnw.cmd test` con **BUILD SUCCESS** y un test de inicio.

Las funcionalidades Android de perfil, favoritos, roles, formularios y descarga de PDF deben comprobarse también durante la revisión final del grupo.

## Consideraciones

- La base de datos H2 es **local al computador que ejecuta el backend**; sus archivos no se incluyen en Git.
- Perfil y favoritos continúan almacenándose **en el dispositivo Android**; no se sincronizan con H2.
- El filtrado de videos por especialidad se realiza desde Android sobre el resultado de `GET /api/videos`.
- Los PDF originales están disponibles como recursos estáticos del backend.
- El formulario de contacto valida y guarda un borrador local, pero no realiza un envío real.
- La URL de Retrofit debe corresponder al entorno de ejecución (emulador o teléfono físico).
- Antes de la evaluación, el grupo debe contrastar la implementación con la pauta oficial de EP3.
