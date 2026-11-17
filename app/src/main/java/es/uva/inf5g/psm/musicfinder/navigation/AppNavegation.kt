package es.uva.inf5g.psm.musicfinder.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import es.uva.inf5g.psm.musicfinder.ui.screens.AlbumDetailScreen
import es.uva.inf5g.psm.musicfinder.ui.screens.FavoritesScreen
import es.uva.inf5g.psm.musicfinder.ui.screens.SearchScreen

// Definimos las rutas de navegación
object Routes {
    const val SEARCH_SCREEN = "search"
    const val ALBUM_DETAIL_SCREEN = "album_detail"
    const val FAVORITES_SCREEN = "favorites"
}

@Composable
fun AppNavigation() {
    // Creamos el controlador de navegación
    val navController = rememberNavController()

    // Configuramos el NavHost con las pantallas
    NavHost(
        navController = navController,
        startDestination = Routes.SEARCH_SCREEN
    ) {
        // Pantalla 1: Búsqueda
        composable(Routes.SEARCH_SCREEN) {
            SearchScreen(navController = navController)
        }

        // Pantalla 2: Detalle del Álbum
        composable("${Routes.ALBUM_DETAIL_SCREEN}/{artist}/{album}") { backStackEntry ->
            // Obtenemos los parámetros de la ruta
            val artist = backStackEntry.arguments?.getString("artist") ?: ""
            val album = backStackEntry.arguments?.getString("album") ?: ""

            AlbumDetailScreen(
                navController = navController,
                artistName = artist,
                albumName = album
            )
        }

        // Pantalla 3: Favoritos
        composable(Routes.FAVORITES_SCREEN) {
            FavoritesScreen(navController = navController)
        }
    }
}