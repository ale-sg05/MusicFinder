package es.uva.inf5g.psm.musicfinder.data.service

import es.uva.inf5g.psm.musicfinder.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//Singleton para crear la instancia de Retrofit
object RetrofitClient {

    //Instancia única de Retrofit
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.LASTFM_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //Propiedad pública para acceder al servicio API
    val lastFmApi: LastFmApiService by lazy {
        retrofit.create(LastFmApiService::class.java)
    }
}