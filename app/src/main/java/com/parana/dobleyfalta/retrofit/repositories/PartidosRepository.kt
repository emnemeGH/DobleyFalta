package com.parana.dobleyfalta.retrofit.repositories

import com.parana.dobleyfalta.retrofit.models.partidos.PartidoModel
import com.parana.dobleyfalta.retrofit.clients.RetrofitClientPartidos
import com.parana.dobleyfalta.retrofit.models.partidos.CrearPartidoModel
import com.parana.dobleyfalta.retrofit.models.partidos.MarcadorUpdateRequest
import com.parana.dobleyfalta.retrofit.viewmodels.partidos.EquipoType
import retrofit2.Response

class PartidosRepository {

    private val api = RetrofitClientPartidos.partidosApiService

    suspend fun obtenerPartidos(): List<PartidoModel> {
        return api.getPartidos()
    }

    suspend fun obtenerPartidosPorJornada(jornadaId: Int): List<PartidoModel> {
        return api.getPartidosPorJornada(jornadaId)
    }

    suspend fun crearPartido(partido: CrearPartidoModel): Response<PartidoModel> {
        return api.crearPartido(partido)
    }

    suspend fun eliminarPartido(id: Int): Boolean {
        val response = api.eliminarPartido(id)
        return response.isSuccessful
    }

    suspend fun actualizarPuntuacionBackend(
        partidoId: Int,
        equipo: EquipoType, // Acepta el Enum
        puntos: Int
    ): Boolean {
        return try {
            // Conversión segura del Enum a String ("LOCAL" o "VISITANTE") para el backend
            val equipoString = when (equipo) {
                EquipoType.LOCAL -> "LOCAL"
                EquipoType.VISITANTE -> "VISITANTE"
            }

            // Creamos el body con el String
            val request = MarcadorUpdateRequest(
                equipo = equipoString, // <-- Usa el String
                puntos = puntos
            )

            // Llamada al endpoint
            val response = api.actualizarPuntaje(partidoId, request)
            response.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

}
