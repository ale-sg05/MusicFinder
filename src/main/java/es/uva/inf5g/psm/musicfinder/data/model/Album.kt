package es.uva.inf5g.psm.musicfinder.data.model

import com.google.gson.annotations.SerializedName

// Modelo principal para álbumes, mapeado desde la API de Last.fm
data class Album(
    val name: String,
    val artist: String,
    val mbid: String? = null,  // ID único (puede ser null)
    @SerializedName("image") val images: List<AlbumImage>,
    @SerializedName("url") val url: String,
    val listeners: String? = null,
    val playcount: String? = null
){
    // Función para obtener la URL de imagen de tamaño específico
    fun getImageUrl(size: String = "medium"): String? {
        return images.find { it.size == size }?.url
            ?: when (size) {
                "medium" -> images.find { it.size == "large" }?.url
                "large" -> images.find { it.size == "extralarge" }?.url
                else -> images.firstOrNull()?.url
            }
    }

    // Función para obtener la mejor imagen disponible
    fun getBestImageUrl(): String? {
        return images.find { it.size == "medium" }?.url
            ?: images.find { it.size == "large" }?.url
            ?: images.find { it.size == "extralarge" }?.url
            ?: images.firstOrNull()?.url
    }

    // Actualizar la función toFavoriteAlbum
    fun toFavoriteAlbum(albumId: String? = null): FavoriteAlbum {
        val id = albumId ?: this.mbid ?: "${this.artist}_${this.name}".hashCode().toString()

        // Obtener la mejor imagen
        val bestImageUrl = this.getBestImageUrl() ?: ""

        return FavoriteAlbum(
            id = id,
            name = this.name,
            artist = this.artist,
            imageUrl = bestImageUrl
            // addedAt se pondrá cuando se guarde en Firestore
        )
    }

    fun getAlbumId(): String {
        return this.mbid ?: "${this.artist}_${this.name}".hashCode().toString()
    }
}

// Modelo para las imágenes del álbum en diferentes tamaños
data class AlbumImage(
    @SerializedName("#text") val url: String,
    @SerializedName("size") val size: String //"small", "medium", "large", "extralarge"
) {
    // Función de ayuda para obtener la URL de la imagen de tamaño medio
    fun getMediumUrl(): String? {
        return if (size == "medium") url else null
    }

    // Función de ayuda para obtener la URL de la imagen de tamaño grande
    fun getLageImage(): String? {
        return if (size == "large") url else null
    }
}

