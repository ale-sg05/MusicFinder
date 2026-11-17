package es.uva.inf5g.psm.musicfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.uva.inf5g.psm.musicfinder.data.model.FavoriteAlbum
import es.uva.inf5g.psm.musicfinder.data.repository.MusicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class FavoritesViewModel(private val repository: MusicRepository) : ViewModel(){

    private val _favorites = MutableStateFlow<List<FavoriteAlbum>>(emptyList())
    val favorites: StateFlow<List<FavoriteAlbum>> = _favorites.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()


    init {
        loadFavorites()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val favoritesList = repository.getFavorites()
                _favorites.value = favoritesList
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar favoritos: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun removeFavorite(favoriteAlbum: FavoriteAlbum) {
        viewModelScope.launch {
            try {
                val success = repository.removeFavorite(favoriteAlbum.id)
                if (success) {
                    _favorites.value = _favorites.value.filter { it.id != favoriteAlbum.id }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al eliminar favorito: ${e.message}"
            }
        }
    }

    fun refresh() {
        loadFavorites()
    }


}