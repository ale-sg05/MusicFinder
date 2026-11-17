package es.uva.inf5g.psm.musicfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.uva.inf5g.psm.musicfinder.data.model.Album
import es.uva.inf5g.psm.musicfinder.data.model.reponse.AlbumDetail
import es.uva.inf5g.psm.musicfinder.data.repository.MusicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AlbumDetailViewModel(
    private val repository: MusicRepository,
    private val artistName: String,
    private val albumName: String
) : ViewModel() {

    // Estados para la UI
    private val _album = MutableStateFlow<Album?>(null)
    val album: StateFlow<Album?> = _album.asStateFlow()

    private val _albumInfo = MutableStateFlow<AlbumDetail?>(null)
    val albumInfo: StateFlow<AlbumDetail?> = _albumInfo.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)


    // Inicializar cargando de los detalles
    init {
        loadAlbumDetails()
    }


    // Cargar detalles del álbum y verificar si es favorito
    fun loadAlbumDetails() {

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                // 1. Cargar información del álbum
                val info = repository.getAlbumInfo(artistName, albumName)

                // 2. Convertir a nuestro modelo Album si es necesario
                info?.let {
                    _album.value = Album(
                        name = it.name,
                        artist = it.artist,
                        images = it.images,
                        url = "",
                        listeners = it.listeners,
                        playcount = it.playcount
                    )
                    // 3. Verificar si es favorito
                    val albumId = "${it.artist}_${it.name}".hashCode().toString()
                    _isFavorite.value = repository.isFavorite(albumId)
                }

                if (info == null) {
                    _errorMessage.value = "No se encontró información del álbum"
                }

            }catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Alternar favorito
    fun toggleFavorite() {
        viewModelScope.launch {
            val currentAlbum = _album.value ?: return@launch
            val albumId = currentAlbum.getAlbumId()

            if (_isFavorite.value) {
                // Quitar de favoritos
                val success = repository.removeFavorite(albumId)
                if (success) {
                    _isFavorite.value = false
                }
            } else {
                // Añadir a favoritos
                val success = repository.addFavorite(currentAlbum)
                if (success) {
                    _isFavorite.value = true
                }
            }
        }
    }

    // Recargar datos
    fun refresh() {
        loadAlbumDetails()
    }
}