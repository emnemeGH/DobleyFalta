package com.parana.dobleyfalta.retrofit.services

import com.parana.dobleyfalta.retrofit.models.equipos.CrearEquipoModel
import com.parana.dobleyfalta.retrofit.models.equipos.EquipoModel
import retrofit2.Response
import retrofit2.http.*

interface EquiposApiService {

    @GET("api/equipos/all")
    suspend fun getEquipos(): List<EquipoModel>

    @GET("api/equipos/{id}")
    suspend fun getEquipoPorId(@Path("id") id: Int): EquipoModel

    @POST("api/equipos/add")
    suspend fun crearEquipo(@Body equipo: CrearEquipoModel): Response<EquipoModel>

    @PUT("api/equipos/{id}")
    suspend fun actualizarEquipo(@Path("id") id: Int, @Body equipo: CrearEquipoModel): EquipoModel

    @DELETE("api/equipos/{id}")
    suspend fun eliminarEquipo(@Path("id") id: Int): Response<Unit>
}