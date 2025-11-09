package com.parana.dobleyfalta.retrofit.repositories

import PartidoModel
import com.parana.dobleyfalta.retrofit.clients.RetrofitClientPartidos
import com.parana.dobleyfalta.retrofit.models.partidos.CrearPartidoModel
import retrofit2.Response

class PartidosRepository {

    private val api = RetrofitClientPartidos.partidosApiService

    suspend fun obtenerPartidos(): List<PartidoModel> {
        return api.getPartidos()
    }

    suspend fun obtenerPartidoPorId(id: Int): PartidoModel {
        return api.getPartidoPorId(id)
    }

    suspend fun obtenerPartidosPorJornada(jornadaId: Int): List<PartidoModel> {
        return api.getPartidosPorJornada(jornadaId)
    }

    suspend fun crearPartido(partido: CrearPartidoModel): Response<PartidoModel> {
        return api.crearPartido(partido)
    }

    suspend fun actualizarPartido(id: Int, partido: CrearPartidoModel): Response<PartidoModel> {
        return api.actualizarPartido(id, partido)
    }

    suspend fun eliminarPartido(id: Int): Boolean {
        val response = api.eliminarPartido(id)
        return response.isSuccessful
    }

}
