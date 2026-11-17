// kotlin
package es.uva.inf5g.psm.musicfinder.ui.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Text as MText
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import es.uva.inf5g.psm.musicfinder.data.model.Album
import es.uva.inf5g.psm.musicfinder.data.model.Genre
import es.uva.inf5g.psm.musicfinder.navigation.Routes
import es.uva.inf5g.psm.musicfinder.util.AppContainer
import es.uva.inf5g.psm.musicfinder.viewmodel.SearchViewModel
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import es.uva.inf5g.psm.musicfinder.viewmodel.SearchViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController) {
    val viewModel: SearchViewModel = viewModel(
        factory = SearchViewModelFactory(AppContainer.musicRepository)
    )

    // Leer directamente los MutableState del ViewModel (se actualizan automáticamente)
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val genreAlbums by viewModel.genreAlbums.collectAsState()
    val selectedGenre by viewModel.selectedGenre.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val favoriteStatus by viewModel.favoriteStatus.collectAsState()

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    MText(
                        "Music Explorer",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(
                        onClick = { navController.navigate(Routes.FAVORITES_SCREEN) }
                    ) {

                            Icon(
                                Icons.Filled.Favorite,
                                contentDescription = "Favoritos",
                            )

                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            //BUSCADOR
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { newQuery ->
                    viewModel.updateSearchQuery(newQuery)
                },
                label = { Text("Buscar álbumes o artistas") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, vertical = 8.dp)
                    .focusRequester(focusRequester),
                trailingIcon = {
                    IconButton(
                        onClick = { viewModel.searchAlbums() },
                        enabled = searchQuery.isNotBlank()
                    ) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    }
                },
                singleLine = true
            )

            /*//FILTRO
            Text(
                text = "Explorar por género:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Genre.entries.forEach { genre ->
                    FilterChip(
                        selected = selectedGenre == genre,
                        onClick = { viewModel.selectGenre(genre) },
                        label = { Text(genre.displayName) }
                    )
                }
            }*/

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                    errorMessage?.let { message ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = message,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                if (genreAlbums.isNotEmpty()) {
                    AlbumGridSection(
                        title = "Álbumes de ${selectedGenre.displayName}",
                        albums = genreAlbums,
                        onAlbumClick = { album ->
                            navController.navigate(
                                "${Routes.ALBUM_DETAIL_SCREEN}/${Uri.encode(album.artist)}/${Uri.encode(album.name)}"
                            )
                        },
                        onFavoriteClick = { album -> viewModel.toggleFavorite(album) }
                    )
                }

                if (searchResults.isNotEmpty()) {
                    AlbumGridSection(
                        title = "Resultados de búsqueda",
                        albums = searchResults,
                        onAlbumClick = { album ->
                            navController.navigate(
                                "${Routes.ALBUM_DETAIL_SCREEN}/${Uri.encode(album.artist)}/${Uri.encode(album.name)}"
                            )
                        },
                        onFavoriteClick = { album -> viewModel.toggleFavorite(album) }
                    )
                }

                if (genreAlbums.isEmpty() && searchResults.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp)
                            )
                            Text("Busca álbumes o selecciona un género")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AlbumGridSection(
    title: String,
    albums: List<Album>,
    onAlbumClick: (Album) -> Unit,
    onFavoriteClick: (Album) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        MText(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(albums) { album ->
                AlbumGridItem(
                    album = album,
                    onClick = { onAlbumClick(album) },
                    onFavoriteClick = { onFavoriteClick(album) }
                )
            }
        }
    }
}

@Composable
fun AlbumGridItem(
    album: Album,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.75f)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(MaterialTheme.shapes.small)
            ) {
                val imageUrl = album.getImageUrl("medium")
                if (!imageUrl.isNullOrEmpty()) {
                    val ctx = LocalContext.current
                    val request = remember(imageUrl) {
                        ImageRequest.Builder(ctx)
                            .data(imageUrl)
                            .crossfade(true)
                            .build()
                    }

                    SubcomposeAsyncImage(
                        model = request,
                        contentDescription = album.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    ) {
                        when (painter.state) {
                            is coil.compose.AsyncImagePainter.State.Loading -> {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                                }
                            }
                            is coil.compose.AsyncImagePainter.State.Error -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(MaterialTheme.colorScheme.surfaceVariant),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.StarBorder,
                                        contentDescription = "Error",
                                        modifier = Modifier.size(32.dp),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            else -> {
                                SubcomposeAsyncImageContent()
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.MusicNote,
                            contentDescription = "Sin imagen",
                            modifier = Modifier.size(32.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                ) {
                    Icon(
                        Icons.Outlined.Star,
                        contentDescription = "Añadir a favoritos",
                        modifier = Modifier
                            .size(28.dp)
                            .clickable { onFavoriteClick() },
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Text(
                text = album.name,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            Text(
                text = album.artist,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
