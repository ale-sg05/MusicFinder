// kotlin
package es.uva.inf5g.psm.musicfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.uva.inf5g.psm.musicfinder.data.model.Album
import es.uva.inf5g.psm.musicfinder.data.model.Genre
import es.uva.inf5g.psm.musicfinder.data.repository.MusicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class SearchViewModel(private val repository: MusicRepository) : ViewModel() {

    // Exponer MutableState para que Compose pueda observar cambios
    val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    val _searchResults = MutableStateFlow<List<Album>>(emptyList())
    val searchResults: StateFlow<List<Album>> = _searchResults.asStateFlow()
    val _genreAlbums = MutableStateFlow<List<Album>>(emptyList())
    val genreAlbums: StateFlow<List<Album>> = _genreAlbums.asStateFlow()
    val _selectedGenre = MutableStateFlow(Genre.POP)
    val selectedGenre: StateFlow<Genre> = _selectedGenre.asStateFlow()
    val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    //Mapa para trackear favoritos
    private val _favoriteStatus = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val favoriteStatus: StateFlow<Map<String, Boolean>> = _favoriteStatus.asStateFlow()



    init {
        loadGenreAlbums(Genre.POP)
    }

    // Actualizar el query de búsqueda
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // Función para buscar álbumes
    fun searchAlbums() {
        if (_searchQuery.value.isBlank()) return

        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                _searchResults.value = repository.searchAlbums(_searchQuery.value)
            } catch (e: Exception) {
                _errorMessage.value = "Error al buscar: ${e.message}"
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Función para cambiar el género
    fun selectGenre(genre: Genre) {
        _selectedGenre.value = genre
        loadGenreAlbums(genre)
    }

    // Función para cargar álbumes por género
    private fun loadGenreAlbums(genre: Genre) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                _genreAlbums.value = repository.getAlbumsByGenre(genre)
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar género: ${e.message}"
                _genreAlbums.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Alternar favorito desde la pantalla de búsqueda
    fun toggleFavorite(album: Album) {
        viewModelScope.launch {
            try {
                val albumId = album.getAlbumId()
                val currentStatus = _favoriteStatus.value[albumId] ?: false

                if (currentStatus) {
                    val success = repository.removeFavorite(albumId)
                    if (success) {
                        _favoriteStatus.value = _favoriteStatus.value - albumId
                    }
                } else {
                    // Añadir a favoritos
                    val success = repository.addFavorite(album)
                    if (success) {
                        _favoriteStatus.value = _favoriteStatus.value + (albumId to true)
                    }
                }
            } catch (_: Exception) {
                // Silenciar error
            }
        }
    }

}
