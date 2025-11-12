package com.parana.dobleyfalta.retrofit.repositories

import com.parana.dobleyfalta.retrofit.clients.RetrofitClientTablas
import com.parana.dobleyfalta.retrofit.models.tablas.TablaDTOModel
//import com.parana.dobleyfalta.retrofit.services.TablasApiService

class TablasRepository {
    private val api = RetrofitClientTablas.tablasApiService

    suspend fun obtenerTablaPorLiga(idLiga: Int): List<TablaDTOModel> {
        return api.getTablaPorLiga(idLiga)
    }
}