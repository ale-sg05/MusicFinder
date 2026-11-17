package es.uva.inf5g.psm.musicfinder.data.model

import java.util.Date

// Modelo para álbumes favoritos guardados en Firestore
data class FavoriteAlbum(
    val id: String = "",
    val name: String = "",
    val artist: String = "",
    val imageUrl: String = "",
    val addedAt: Date = Date()  // Fecha cuando se añadió a favoritos
) {
    // Constructor vacío para Firestore
    constructor() : this("", "", "", "", Date())

    fun toLogString(): String{
        return "FavoriteAlbum: $name - $artist (ID: $id)"
    }
}