package com.parana.dobleyfalta.retrofit.services

import com.parana.dobleyfalta.retrofit.models.jornadas.CrearJornadaModel
import com.parana.dobleyfalta.retrofit.models.jornadas.JornadaModel
import retrofit2.Response
import retrofit2.http.*

interface JornadasApiService {

    @GET("api/jornada/all")
    suspend fun getJornadas(): List<JornadaModel>

    @GET("api/jornada/{id}")
    suspend fun getJornadaPorId(@Path("id") id: Int): JornadaModel

    @POST("api/jornada/add")
    suspend fun crearJornada(@Body jornada: CrearJornadaModel): Response<JornadaModel>

    @PUT("api/jornada/{id}")
    suspend fun editarJornada(
        @Path("id") id: Int,
        @Body jornada: CrearJornadaModel
    ): Response<JornadaModel>

    @DELETE("api/jornada/{id}")
    suspend fun eliminarJornada(@Path("id") id: Int): Response<Unit>
}
