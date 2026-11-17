package es.uva.inf5g.psm.musicfinder.data.service

import es.uva.inf5g.psm.musicfinder.data.model.reponse.AlbumDetailReponse
import es.uva.inf5g.psm.musicfinder.data.model.reponse.AlbumSearchReponse
import es.uva.inf5g.psm.musicfinder.data.model.reponse.TagTopAlbumsReponse
import es.uva.inf5g.psm.musicfinder.util.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface LastFmApiService {

    //Endpoint 1: Buscar álbumes por término (artista o nombre de álbum)
    @GET("2.0/")
    suspend fun searchAlbum(
        @Query("method") method: String = "album.search",
        @Query("album") query: String,
        @Query("api_key") apiKey: String = Constants.LASTFM_API_KEY,
        @Query("format") format: String = "json",
        @Query("limit") limit: Int = 20
    ): AlbumSearchReponse

    //Endpoint 2: Obtener información detallada de un álbum específico
    @GET("2.0/")
    suspend fun getAlbumInfo(
        @Query("method") method: String = "album.getinfo",
        @Query("artist") artist: String,
        @Query("album") album: String,
        @Query("api_key") apiKey: String = Constants.LASTFM_API_KEY,
        @Query("format") format: String = "json"
    ): AlbumDetailReponse

    // Endpoint 3: Obtener álbumes por tag (género)
    @GET("2.0/")
    suspend fun getTopAlbumsByTag(
        @Query("method") method: String = "tag.gettopalbums",
        @Query("tag") tag: String,
        @Query("api_key") apiKey: String = Constants.LASTFM_API_KEY,
        @Query("format") format: String = "json",
        @Query("limit") limit: Int = 15
    ): TagTopAlbumsReponse
}