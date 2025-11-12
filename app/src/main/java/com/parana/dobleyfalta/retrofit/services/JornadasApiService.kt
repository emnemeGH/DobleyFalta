package com.parana.dobleyfalta.retrofit.services

import com.parana.dobleyfalta.retrofit.models.partidos.PartidoModel
import com.parana.dobleyfalta.retrofit.models.jornadas.CrearJornadaModel
import com.parana.dobleyfalta.retrofit.models.jornadas.JornadaModel
import retrofit2.Response
import retrofit2.http.*

interface JornadasApiService {

    @GET("api/jornadas/all")
    suspend fun getJornadas(): List<JornadaModel>

    @GET("api/jornadas/{id}")
    suspend fun getJornadaPorId(@Path("id") id: Int): JornadaModel

    @GET("api/jornadas/ligas/{ligaId}")   // <-- ¡Este es el endpoint clave!
    suspend fun getJornadasPorLiga(@Path("ligaId") ligaId: Int): List<JornadaModel>

    @POST("api/jornadas/add")
    suspend fun crearJornada(@Body jornada: CrearJornadaModel): Response<JornadaModel>

    @PUT("api/jornadas/{id}")
    suspend fun editarJornada(
        @Path("id") id: Int,
        @Body jornada: CrearJornadaModel
    ): Response<JornadaModel>

    @DELETE("api/jornadas/{id}")
    suspend fun eliminarJornada(@Path("id") id: Int): Response<Unit>

    @GET("api/partidos/jornada/{jornadaId}")  // Endpoint que devuelva los partidos de una jornada
    suspend fun getPartidosPorJornada(@Path("jornadaId") jornadaId: Int): List<PartidoModel>
}
