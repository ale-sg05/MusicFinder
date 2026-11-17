package es.uva.inf5g.psm.musicfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import es.uva.inf5g.psm.musicfinder.data.repository.MusicRepository


class SearchViewModelFactory(
    private val repository: MusicRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel desconocido")
    }
}

class AlbumDetailViewModelFactory(
    private val repository: MusicRepository,
    private val artistName: String,
    private val albumName: String
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlbumDetailViewModel::class.java)) {
            return AlbumDetailViewModel(repository, artistName, albumName) as T
        }
        throw IllegalArgumentException("ViewModel desconocido")
    }
}

class FavoritesViewModelFactory(
    private val repository: MusicRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            return FavoritesViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel desconocido")
    }
}