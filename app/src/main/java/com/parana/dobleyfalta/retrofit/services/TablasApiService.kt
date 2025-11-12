package com.parana.dobleyfalta.retrofit.services

import com.parana.dobleyfalta.retrofit.models.tablas.TablaDTOModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface TablasApiService {

    @GET("api/tablas/liga/{idLiga}")
    suspend fun getTablaPorLiga(@Path("idLiga") idLiga: Int): List<TablaDTOModel>

}