package es.uva.inf5g.psm.musicfinder.util

object Constants {
    // API de Last.fm
// Constants.kt: sustituye la línea de la clave
    val LASTFM_API_KEY: String = es.uva.inf5g.psm.musicfinder.BuildConfig.LASTFM_API_KEY
    const val LASTFM_BASE_URL = "https://ws.audioscrobbler.com/"

    // Colecciones de Firebase
    const val FAVORITES_COLLECTION = "favorite_albums"

    // Nombres de pantallas para navegación
    const val ROUTE_SEARCH = "search"
    const val ROUTE_ALBUM_DETAIL = "album_detail"
    const val ROUTE_FAVORITES = "favorites"

    const val ITEMS_PER_ROW = 3 //Usar en el grid de albumes
}