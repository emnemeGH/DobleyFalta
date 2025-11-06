package com.parana.dobleyfalta.retrofit.repositories

import com.parana.dobleyfalta.retrofit.clients.RetrofitClientEquipos
import com.parana.dobleyfalta.retrofit.models.equipos.CrearEquipoModel
import com.parana.dobleyfalta.retrofit.models.equipos.EquipoModel
import retrofit2.Response

class EquiposRepository {

    private val api = RetrofitClientEquipos.equiposApiService

    suspend fun obtenerEquipos(): List<EquipoModel> {
        return api.getEquipos()
    }

    suspend fun obtenerEquipoPorId(id: Int): EquipoModel {
        return api.getEquipoPorId(id)
    }

    suspend fun crearEquipo(equipo: CrearEquipoModel): Response<EquipoModel> {
        return api.crearEquipo(equipo)
    }

    suspend fun actualizarEquipo(id: Int, equipo: CrearEquipoModel): EquipoModel {
        return api.actualizarEquipo(id, equipo)
    }

    suspend fun eliminarEquipo(id: Int): Boolean {
        val response = api.eliminarEquipo(id)
        return response.isSuccessful
    }
}