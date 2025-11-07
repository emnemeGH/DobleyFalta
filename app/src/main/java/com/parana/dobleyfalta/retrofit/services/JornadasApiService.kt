package com.parana.dobleyfalta.retrofit.services

import com.parana.dobleyfalta.retrofit.models.jornadas.CrearJornadaModel
import com.parana.dobleyfalta.retrofit.models.jornadas.JornadaModel
import com.parana.dobleyfalta.retrofit.models.partidos.PartidoModel
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

    // --- NUEVOS MÉTODOS ---
    @GET("api/jornada/ligas/{ligaId}")   // Endpoint que devuelva las jornadas de una liga
    suspend fun getJornadasPorLiga(@Path("ligaId") ligaId: Int): List<JornadaModel>

    @GET("api/partidos/jornada/{jornadaId}")  // Endpoint que devuelva los partidos de una jornada
    suspend fun getPartidosPorJornada(@Path("jornadaId") jornadaId: Int): List<PartidoModel>
}
