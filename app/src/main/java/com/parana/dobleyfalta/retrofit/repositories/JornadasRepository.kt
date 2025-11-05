package com.parana.dobleyfalta.retrofit.repositories

import com.parana.dobleyfalta.retrofit.clients.RetrofitClientJornadas
import com.parana.dobleyfalta.retrofit.models.jornadas.CrearJornadaModel
import com.parana.dobleyfalta.retrofit.models.jornadas.JornadaModel
import retrofit2.Response

class JornadasRepository {

    private val api = RetrofitClientJornadas.jornadasApiService

    suspend fun obtenerJornadas(): List<JornadaModel> {
        return api.getJornadas()
    }

    suspend fun obtenerJornadaPorId(id: Int): JornadaModel {
        return api.getJornadaPorId(id)
    }

    suspend fun crearJornada(jornada: CrearJornadaModel): Response<JornadaModel> {
        return api.crearJornada(jornada)
    }

    suspend fun editarJornada(id: Int, jornada: CrearJornadaModel): Response<JornadaModel> {
        return api.editarJornada(id, jornada)
    }

    suspend fun eliminarJornada(id: Int): Boolean {
        val response = api.eliminarJornada(id)
        return response.isSuccessful
    }
}
