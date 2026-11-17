package es.uva.inf5g.psm.musicfinder.data.service

import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import es.uva.inf5g.psm.musicfinder.data.model.Album
import es.uva.inf5g.psm.musicfinder.data.model.FavoriteAlbum
import es.uva.inf5g.psm.musicfinder.util.Constants
import kotlinx.coroutines.tasks.await
import java.util.Date

class FirebaseService {

    private val db = Firebase.firestore
    private val favoritesCollection = db.collection(Constants.FAVORITES_COLLECTION)

    suspend fun addFavorite(album: Album): Boolean {
        return try {
            val albumId = album.getAlbumId()

            //Obtener mejor imagen posible
            val favoriteAlbum = album.toFavoriteAlbum(albumId)

            //Guarder en firestore el siguiente objeto
            val favoriteData = hashMapOf(
                "albumId" to albumId,
                "albumName" to favoriteAlbum.name,
                "artist" to favoriteAlbum.artist,
                "imageUrl" to  favoriteAlbum.imageUrl,
                "addedAt" to FieldValue.serverTimestamp()
            )

            favoritesCollection.document(albumId).set(favoriteData).await()
            println("DEBUG: Álbum guardado como favorito: ${album.name}")
            true
        } catch (e: Exception){
            println("DEBUG: Error al guardar favorito: ${e.message}")
            false
        }
    }

    suspend fun removeFavorite(albumId: String): Boolean{
        return try {
            favoritesCollection.document(albumId).delete().await()
            println("DEBUG: Álbum eliminado de favoritos: $albumId")
            true
        }catch (e: Exception){
            println("DEBUG: Error al eliminar favorito: ${e.message}")
            false
        }
    }

    suspend fun getFavorites(): List<FavoriteAlbum> {
        return try {

            val querySnapshot = favoritesCollection.orderBy("addedAt").get().await()

            querySnapshot.documents.map { document ->
                FavoriteAlbum(
                    id = document.getString("albumId") ?: "",
                    name = document.getString("albumName") ?: "",
                    artist = document.getString("artist") ?: "",
                    imageUrl = document.getString("imageUrl") ?: "",
                    addedAt = document.getDate("addedAt") ?: Date()
                )
            }
        } catch (e: Exception){
            emptyList()
        }
    }

    suspend fun isfavorite(albumId: String): Boolean{
        return try {
            val document = favoritesCollection.document(albumId).get().await()
            document.exists()
        }catch (e: Exception){
            false
        }
    }

}