package es.uva.inf5g.psm.musicfinder


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import es.uva.inf5g.psm.musicfinder.navigation.Screen
import es.uva.inf5g.psm.musicfinder.ui.screens.AlbumDetailScreen
import es.uva.inf5g.psm.musicfinder.ui.screens.FavoritesScreen
import es.uva.inf5g.psm.musicfinder.ui.screens.SearchScreen
import com.google.firebase.FirebaseApp
import es.uva.inf5g.psm.musicfinder.navigation.AppNavigation
import es.uva.inf5g.psm.musicfinder.navigation.MusicFinder
import es.uva.inf5g.psm.musicfinder.ui.theme.MusicFinderTheme
import es.uva.inf5g.psm.musicfinder.viewmodel.AlbumDetailViewModel
import es.uva.inf5g.psm.musicfinder.viewmodel.FavoritesViewModel
import kotlinx.coroutines.delay


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar Firebase
        FirebaseApp.initializeApp(this)

        // Splash screen mientras se inicializa
        setContent {
            MusicFinderTheme {
                var isLoading by remember { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    // Simular tiempo de carga (1 segundo)
                    delay(1000)
                    isLoading = false
                }

                if (isLoading) {
                    // Splash screen
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.MusicNote,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Music Explorer",
                                style = MaterialTheme.typography.headlineLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                } else {
                    // App principal
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        Surface(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            AppNavigation()
                        }
                    }
                }
            }
        }
    }
}