package es.uva.inf5g.psm.musicfinder.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument


@Composable
fun MusicFinder(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Search.route
    ){
        composable(Screen.Search.route){
            //pantalla de busqueda
        }

        composable(
            route = "${Screen.AlbumDetail.route}/{artist}/{albumName}",
            arguments = listOf(
                navArgument("artist") { type = androidx.navigation.NavType.StringType },
                navArgument("album_name") { type = androidx.navigation.NavType.StringType }
            )
        ) { backStackEntry -> //pantalla de detalle
            val artist = backStackEntry.arguments?.getString("artist") ?: ""
            val albumName = backStackEntry.arguments?.getString("albumName") ?: ""
        }

        composable(Screen.Favorites.route) {
            //Favoritos
        }
    }
}