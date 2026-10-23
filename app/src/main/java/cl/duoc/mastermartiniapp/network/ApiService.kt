package cl.duoc.mastermartiniapp.network

import cl.duoc.mastermartiniapp.model.Categoria
import cl.duoc.mastermartiniapp.model.Video
import retrofit2.http.GET

interface ApiService {

    @GET("api/categorias")
    suspend fun obtenerCategorias(): List<Categoria>

    @GET("api/videos")
    suspend fun obtenerVideos(): List<Video>
}