package cl.duoc.mastermartiniapp.repository

import cl.duoc.mastermartiniapp.model.Categoria
import cl.duoc.mastermartiniapp.network.ApiService
import cl.duoc.mastermartiniapp.network.ClienteRetrofit

class CategoriasRepository(
    private val api: ApiService = ClienteRetrofit.api
) {

    suspend fun obtenerCategorias(): List<Categoria> {
        return api.obtenerCategorias()
    }
}