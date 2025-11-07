package com.parana.dobleyfalta.retrofit.repositories

import com.parana.dobleyfalta.retrofit.clients.RetrofitClientLigas
import com.parana.dobleyfalta.retrofit.models.ligas.CrearLigaModel
import com.parana.dobleyfalta.retrofit.models.ligas.LigaModel
import com.parana.dobleyfalta.retrofit.models.ligas.LigaUpdateModel
import retrofit2.Response

class LigasRepository {

    private val api = RetrofitClientLigas.ligasApiService

    suspend fun obtenerLigas(): List<LigaModel> {
        return api.getLigas()
    }

    suspend fun crearLiga(liga: CrearLigaModel): Response<LigaModel> {
        return api.crearLiga(liga)
    }

    suspend fun actualizarLiga(id: Int, liga: LigaUpdateModel): Response<LigaModel> {
        return api.actualizarLiga(id, liga)
    }

    suspend fun eliminarLiga(id: Int): Boolean {
        val response = api.eliminarLiga(id)
        return response.isSuccessful
    }
}
