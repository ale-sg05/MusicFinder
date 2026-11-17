package es.uva.inf5g.psm.musicfinder.data.model.reponse

import com.google.gson.annotations.SerializedName
import es.uva.inf5g.psm.musicfinder.data.model.Album
import es.uva.inf5g.psm.musicfinder.data.model.AlbumImage

data class AlbumSearchReponse(
    @SerializedName("results") val results: AlbumSearchResults
)

data class  AlbumSearchResults(
    @SerializedName("albummatches") val albumMatches: AlbumMatches
)

data class AlbumMatches(
    @SerializedName("album") val albums: List<Album>
)

//es la respuesta de la api para top albums de un tag
data class TagTopAlbumsReponse(
    val albums: TagAlbumsContainer
)

data class TagAlbumsContainer(
    @SerializedName("album") val albums: List<Album>
)

//Respuesta de la api para album info
data class AlbumDetailReponse(
    @SerializedName("album") val album: AlbumDetail
)

data class AlbumDetail(
    val name: String,
    val artist: String,
    @SerializedName("image") val images: List<AlbumImage>,
    val listeners : String,
    val playcount : String,
    val tracks : AlbumTracks?,
    val tags: AlbumTags?,
    val wiki: AlbumWiki?
)

data class AlbumTracks(
    val track: List<TrackDetail>
)

data class TrackDetail(
    val name: String,
    val duration: String,
    @SerializedName("@attr") val attr: TrackAttr?
)

data class TrackAttr(
    val rank: String
)

data class AlbumTags(
    val tag: List<TagDetail>
)

data class TagDetail(
    val name: String,
    val url: String
)

data class AlbumWiki(
    val summary: String,
    val published: String
)

data class ApiImage(
    @SerializedName("#text") val url: String,
    @SerializedName("size") val size: String
)