package cl.duoc.mastermartiniapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ClienteRetrofit {

    private const val BASE_URL =
        /*"http://10.0.2.2:8080/"*/
        "http://192.168.1.11:8080/"

    fun resolverUrl(ruta: String): String =
        if (ruta.startsWith("http://") || ruta.startsWith("https://")) ruta
        else BASE_URL + ruta.removePrefix("/")

    val api: ApiService by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(ApiService::class.java)
    }
}
