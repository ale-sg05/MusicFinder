package es.uva.inf5g.psm.musicfinder.data.repository

import es.uva.inf5g.psm.musicfinder.data.model.Album
import es.uva.inf5g.psm.musicfinder.data.model.FavoriteAlbum
import es.uva.inf5g.psm.musicfinder.data.model.Genre
import es.uva.inf5g.psm.musicfinder.data.model.reponse.AlbumDetail
import es.uva.inf5g.psm.musicfinder.data.service.FirebaseService
import es.uva.inf5g.psm.musicfinder.data.service.LastFmApiService


class MusicRepository(
    private val apiService: LastFmApiService,
    private val firebaseService: FirebaseService

){
    suspend fun searchAlbums(query: String): List<Album>{
        return try {
            val reponse = apiService.searchAlbum(query = query)
            reponse.results.albumMatches.albums
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getAlbumInfo(artist: String, albumName: String): AlbumDetail? {
        return try {
            val response = apiService.getAlbumInfo(artist = artist, album = albumName)
            response.album
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getAlbumInfoAsAlbum(artist: String, albumName: String): Album? {
        val albumInfo = getAlbumInfo(artist, albumName) ?: return null

        return Album(
            name = albumInfo.name,
            artist = albumInfo.artist,
            images = albumInfo.images,
            url = "",
            listeners = albumInfo.listeners,
            playcount = albumInfo.playcount
        )
    }

    suspend fun getAlbumsByGenre(genre: Genre): List<Album> {
        return try {
            val response = apiService.getTopAlbumsByTag(tag = genre.tag)
            response.albums.albums
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun addFavorite(album: Album): Boolean {
        return firebaseService.addFavorite(album)
    }

    suspend fun removeFavorite(albumId: String): Boolean {
        return firebaseService.removeFavorite(albumId)
    }

    suspend fun getFavorites(): List<FavoriteAlbum>{
        return firebaseService.getFavorites()
    }

    suspend fun isFavorite(albumId: String): Boolean {
        return firebaseService.isfavorite(albumId)
    }
}