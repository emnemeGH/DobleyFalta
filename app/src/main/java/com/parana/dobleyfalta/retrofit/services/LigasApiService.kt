package com.parana.dobleyfalta.retrofit.services

import com.parana.dobleyfalta.retrofit.models.ligas.CrearLigaModel
import com.parana.dobleyfalta.retrofit.models.ligas.LigaModel
import com.parana.dobleyfalta.retrofit.models.ligas.LigaUpdateModel
import retrofit2.Response
import retrofit2.http.*

interface LigasApiService {

    @GET("api/liga/all")
    suspend fun getLigas(): List<LigaModel>

    @GET("api/liga/{id}")
    suspend fun getLigaPorId(@Path("id") id: Int): LigaModel

    @POST("api/liga/add")
    suspend fun crearLiga(@Body liga: CrearLigaModel): Response<LigaModel>

    @PUT("api/liga/{id}")
    suspend fun actualizarLiga(
        @Path("id") id: Int,
        @Body liga: LigaUpdateModel
    ): Response<LigaModel>

    @DELETE("api/liga/{id}")
    suspend fun eliminarLiga(@Path("id") id: Int): Response<Unit>
}
