package es.uva.inf5g.psm.musicfinder.util

import es.uva.inf5g.psm.musicfinder.data.repository.MusicRepository
import es.uva.inf5g.psm.musicfinder.data.service.FirebaseService
import es.uva.inf5g.psm.musicfinder.data.service.LastFmApiService
import es.uva.inf5g.psm.musicfinder.data.service.RetrofitClient

object AppContainer {
    // API de Last.fm (Retrofit)
    val lastFmApi: LastFmApiService by lazy {
        RetrofitClient.lastFmApi
    }

    // Servicio de Firebase
    val firestoreService: FirebaseService by lazy {
        FirebaseService()
    }

    // Repositorio principal
    val musicRepository: MusicRepository by lazy {
        MusicRepository(lastFmApi, firestoreService)
    }
}