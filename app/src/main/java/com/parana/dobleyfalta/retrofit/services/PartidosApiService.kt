package com.parana.dobleyfalta.retrofit.services

import com.parana.dobleyfalta.retrofit.models.partidos.PartidoModel
import com.parana.dobleyfalta.retrofit.models.partidos.CrearPartidoModel
import com.parana.dobleyfalta.retrofit.models.partidos.MarcadorUpdateRequest
import retrofit2.Response
import retrofit2.http.*

interface PartidosApiService {

    // Lista simple (PartidoModel)
    @GET("api/partidos/all")
    suspend fun getPartidos(): List<PartidoModel>

    // Devuelve detalle simple por id
    @GET("api/partidos/{id}")
    suspend fun getPartidoPorId(@Path("id") id: Int): PartidoModel

    @GET("api/partidos/jornadas/{jornadaId}")
    suspend fun getPartidosPorJornada(@Path("jornadaId") jornadaId: Int): List<PartidoModel>

    @POST("api/partidos/add")
    suspend fun crearPartido(@Body partido: CrearPartidoModel): Response<PartidoModel>

    @PUT("api/partidos/{id}")
    suspend fun actualizarPartido(@Path("id") id: Int, @Body partido: CrearPartidoModel): Response<PartidoModel>

    @PUT("api/partidos/{id}/puntaje") // <-- Nuevo endpoint
    suspend fun actualizarPuntaje(
        @Path("id") id: Int,
        @Body request: MarcadorUpdateRequest
    ): Response<Void> // Usamos Response<Void> o Response<Unit> si el backend no devuelve cuerpo

    @DELETE("api/partidos/{id}")
    suspend fun eliminarPartido(@Path("id") id: Int): Response<Unit>

}
