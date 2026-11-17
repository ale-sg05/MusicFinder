# 🎵 MusicFinder

Aplicación Android para **buscar información musical**: encuentra álbumes por nombre, explora los más populares de cada género y guarda tus favoritos en la nube.

Proyecto académico de desarrollo Android con **Jetpack Compose** (Universidad de Valladolid). Está pensado a propósito para ser **sencillo y fácil de entender**: pocas pantallas, una arquitectura clara y solo las piezas necesarias.

## ✨ Funcionalidades

- 🔎 **Buscador** de álbumes por nombre; cada resultado muestra portada, álbum y artista.
- 🎸 **Explorar por género**: Pop, Rock, Hip Hop, Jazz, Electronic, Dance, Reggae, Country y Classical.
- 💿 **Detalle del álbum**: portada, nombre, artista, número de oyentes y de reproducciones.
- ❤️ **Favoritos**: añade o quita álbumes y consúltalos en una pantalla propia (guardados en Firebase Firestore).
- ⏳ Indicador de carga y pantalla de error con opción de reintentar.

## 🧭 Pantallas y navegación

| Pantalla | Ruta | Descripción |
|---|---|---|
| Búsqueda | `search` | Pantalla inicial: buscador y selector de género |
| Detalle del álbum | `album_detail/{artist}/{album}` | Recibe el artista y el álbum como parámetros |
| Favoritos | `favorites` | Lista de álbumes guardados |

La navegación usa `NavHost` + `NavController`, con flecha de retroceso en la barra superior (`navigationIcon`).

## 🏗️ Arquitectura (MVVM)

```
app/src/main/java/es/uva/inf5g/psm/musicfinder/
├── MainActivity.kt
├── data/
│   ├── model/            # Data classes (Album, Artist, Genre, FavoriteAlbum...) y respuestas JSON
│   ├── repository/       # MusicRepository: única fuente de datos para los ViewModels
│   └── service/          # Retrofit (Last.fm) y Firestore
├── viewmodel/            # SearchViewModel, AlbumDetailViewModel, FavoritesViewModel + Factories
├── ui/
│   ├── screens/          # Pantallas Compose (solo UI)
│   └── theme/
├── navigation/           # NavHost y rutas
└── util/                 # AppContainer, constantes, mapeadores, pantalla de error
```

**Flujo:** `Pantalla (Compose)` → `ViewModel` → `MusicRepository` → `Last.fm (Retrofit)` / `Firestore`

## ✅ Criterios de la práctica

| Criterio | Cómo se cumple |
|---|---|
| **MVVM** (obligatorio) | Vistas Compose, ViewModels con el estado de la UI y modelos en `data/` |
| **Patrón Repositorio** | `MusicRepository` centraliza API y Firebase; los ViewModels solo acceden a él |
| **Patrón Factory** | `ViewModelProvider.Factory` para los tres ViewModels (reciben repositorio y parámetros) |
| **API externa** (obligatorio) | Retrofit + Gson contra la API de Last.fm, con corrutinas (`suspend`) |
| **Navegación** (obligatorio) | 3 pantallas con `NavHost`/`NavController` y paso de parámetros |
| **Base de datos** | Firebase Firestore para los favoritos (alternativa a Room) |

## 🛠️ Tecnologías

Kotlin · Jetpack Compose (Material 3) · Navigation Compose · ViewModel · Retrofit 2 + Gson + OkHttp Logging · Coroutines · Coil · Firebase Firestore

Requisitos: Android Studio reciente, `minSdk 24`, `compileSdk 36`, JDK 11 o superior.

## 🚀 Cómo ejecutarlo

El repositorio **no incluye claves ni configuración privada**, así que antes de compilar necesitas dos cosas:

1. **Clave de la API de Last.fm**
   Crea una cuenta gratuita en <https://www.last.fm/api/account/create> y añade la clave a `local.properties` (en la raíz del proyecto):
   ```properties
   LASTFM_API_KEY=tu_clave_aqui
   ```
2. **Proyecto de Firebase**
   Crea un proyecto en la [consola de Firebase](https://console.firebase.google.com/), registra una app Android con el paquete `es.uva.inf5g.psm.musicfinder`, activa **Firestore Database** y descarga `google-services.json` a la carpeta `app/`.

Después abre el proyecto en Android Studio, sincroniza Gradle y ejecútalo en un emulador o dispositivo (necesita conexión a Internet).

## 🔮 Posibles mejoras

- Mostrar la lista de canciones, las etiquetas y la reseña del álbum en el detalle.
- Búsqueda de artistas y de canciones, además de álbumes.
- Caché local con Room para uso sin conexión.
- Autenticación para que cada usuario tenga sus propios favoritos.

## 👤 Autoría

Proyecto realizado por **Alejandro** — Universidad de Valladolid, Campus de Segovia.
