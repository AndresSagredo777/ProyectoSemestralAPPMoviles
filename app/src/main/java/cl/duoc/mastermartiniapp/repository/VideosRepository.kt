package cl.duoc.mastermartiniapp.repository

import cl.duoc.mastermartiniapp.model.Video
import cl.duoc.mastermartiniapp.network.ApiService
import cl.duoc.mastermartiniapp.network.ClienteRetrofit

class VideosRepository(
    private val api: ApiService = ClienteRetrofit.api
) {

    suspend fun obtenerVideos(): List<Video> {
        return api.obtenerVideos()
    }
}