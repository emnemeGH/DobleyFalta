package com.parana.dobleyfalta.retrofit.services

import com.parana.dobleyfalta.retrofit.models.partidos.CrearPartidoModel
import com.parana.dobleyfalta.retrofit.models.partidos.PartidoModel
import com.parana.dobleyfalta.retrofit.models.partidos.PartidoDTOModel
import retrofit2.Response
import retrofit2.http.*

interface PartidosApiService {

    // Lista simple (PartidoModel)
    @GET("api/partido/all")
    suspend fun getPartidos(): List<PartidoModel>

    // Devuelve detalle simple por id
    @GET("api/partido/{id}")
    suspend fun getPartidoPorId(@Path("id") id: Int): PartidoModel

    // Si tu backend tiene método que devuelve DTO con equipos embebidos, expónlo:
    // Ej: GET /api/partido/allConEquipos   (ajusta si existe)
    // @GET("api/partido/allConEquipos")
    // suspend fun getPartidosConEquipos(): List<PartidoDTOModel>

    @POST("api/partido/add")
    suspend fun crearPartido(@Body partido: CrearPartidoModel): Response<PartidoModel>

    @PUT("api/partido/{id}")
    suspend fun actualizarPartido(@Path("id") id: Int, @Body partido: CrearPartidoModel): Response<PartidoModel>

    @DELETE("api/partido/{id}")
    suspend fun eliminarPartido(@Path("id") id: Int): Response<Unit>

    // Si tu servicio expone getPartidoConEquipos por id:
    // @GET("api/partido/conEquipos/{id}")
    // suspend fun getPartidoConEquipos(@Path("id") id: Int): PartidoDTOModel
}
