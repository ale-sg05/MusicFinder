package es.uva.inf5g.psm.musicfinder.navigation

sealed class Screen(val route: String){
    data object Search: Screen("search")
    data object AlbumDetail: Screen("album_detail")
    data object Favorites: Screen("favorites")
}
