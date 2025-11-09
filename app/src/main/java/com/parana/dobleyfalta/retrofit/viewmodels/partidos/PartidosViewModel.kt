package com.parana.dobleyfalta.retrofit.viewmodels.partidos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parana.dobleyfalta.retrofit.models.equipos.EquipoModel
import com.parana.dobleyfalta.retrofit.models.partidos.PartidoDTOModel
import com.parana.dobleyfalta.retrofit.repositories.EquiposRepository
import com.parana.dobleyfalta.retrofit.repositories.PartidosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PartidosViewModel : ViewModel() {

    private val repository = PartidosRepository()
    private val equiposRepository = EquiposRepository()

    private val _partidosDTO = MutableStateFlow<List<PartidoDTOModel>>(emptyList())
    val partidosDTO = _partidosDTO.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private var equipos: List<EquipoModel> = emptyList()

    fun cargarPartidos(jornadaId: Int) {
        viewModelScope.launch {
            _loading.value = true
            try {
                if (equipos.isEmpty()) {
                    equipos = equiposRepository.obtenerEquipos()
                }

                val partidos = repository.obtenerPartidosPorJornada(jornadaId)

                _partidosDTO.value = partidos.map { partido ->
                    val equipoLocal = equipos.find { it.idEquipo == partido.idEquipoLocal }
                    val equipoVisitante = equipos.find { it.idEquipo == partido.idEquipoVisitante }

                    PartidoDTOModel(
                        idPartido = partido.idPartido,
                        fecha = partido.fecha,
                        puntosLocal = partido.puntosLocal,
                        puntosVisitante = partido.puntosVisitante,
                        jornadaId = partido.jornada?.idJornada ?: 0,
                        jornadaNumero = partido.jornada?.numero ?: 0,
                        estadoPartido = partido.estadoPartido,
                        equipoLocal = equipoLocal?.nombre,
                        equipoVisitante = equipoVisitante?.nombre,
                        logoLocal = equipoLocal?.logo,
                        logoVisitante = equipoVisitante?.logo
                    )
                }
            } catch (e: Exception) {
                _error.value = "Error al cargar partidos de jornada $jornadaId: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun obtenerPartidosPorJornada(jornadaId: Int): List<PartidoDTOModel> {
        return _partidosDTO.value.filter { it.jornadaId == jornadaId }
    }

    fun clearError() { _error.value = null }
}

