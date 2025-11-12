package com.parana.dobleyfalta.retrofit.viewmodels.partidos

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parana.dobleyfalta.retrofit.models.equipos.EquipoModel
import com.parana.dobleyfalta.retrofit.models.partidos.PartidoDTOModel
import com.parana.dobleyfalta.retrofit.repositories.EquiposRepository
import com.parana.dobleyfalta.retrofit.repositories.PartidosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// NUEVO: Enum para identificar el equipo a actualizar
enum class EquipoType { LOCAL, VISITANTE }

class PartidosViewModel : ViewModel() {

    private val repository = PartidosRepository()
    private val equiposRepository = EquiposRepository()

    private val _partidosDTO = MutableStateFlow<List<PartidoDTOModel>>(emptyList())
    val partidosDTO = _partidosDTO.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    // Caching de equipos para evitar llamadas repetidas
    private var equipos: List<EquipoModel> = emptyList()

    fun cargarTodosLosPartidos() {
        viewModelScope.launch {
            _loading.value = true
            try {
                if (equipos.isEmpty()) {
                    equipos = equiposRepository.obtenerEquipos()
                }

                val partidos = repository.obtenerPartidos()

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
                        equipoLocalId = partido.idEquipoLocal ?: 0,
                        equipoVisitanteId = partido.idEquipoVisitante ?: 0,
                        equipoLocalNombre = equipoLocal?.nombre,
                        equipoVisitanteNombre = equipoVisitante?.nombre,
                        logoLocal = equipoLocal?.logo,
                        logoVisitante = equipoVisitante?.logo
                    )
                }
            } catch (e: Exception) {
                _error.value = "Error al cargar TODOS los partidos: ${e.message}"
                Log.e("PartidosViewModel", "Error al cargar todos los partidos", e)
            } finally {
                _loading.value = false
            }
        }
    }

    fun cargarPartidos(jornadaId: Int) {
        viewModelScope.launch {
            _loading.value = true
            try {
                // Solo cargamos equipos una vez si la caché está vacía
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
                        equipoLocalId = partido.idEquipoLocal ?: 0,
                        equipoVisitanteId = partido.idEquipoVisitante ?: 0,
                        equipoLocalNombre = equipoLocal?.nombre,
                        equipoVisitanteNombre = equipoVisitante?.nombre,
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

    // 💡 NUEVO: Función para eliminar un partido
    fun eliminarPartido(id: Int, jornadaId: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _error.value = null
            try {
                val exito = repository.eliminarPartido(id)
                if (exito) {
                    // Actualizar la lista en memoria eliminando el partido
                    _partidosDTO.value = _partidosDTO.value.filter { it.idPartido != id }
                    onSuccess()
                } else {
                    _error.value = "Error al eliminar el partido. El servidor no confirmó la eliminación."
                }
            } catch (e: Exception) {
                _error.value = "Error al conectar con el servidor para eliminar el partido."
                e.printStackTrace()
            }
        }
    }

    fun clearError() { _error.value = null }

    fun actualizarPuntuacion(
        partidoId: Int,
        equipo: EquipoType,
        nuevoPunto: Int
    ) {
        viewModelScope.launch {
            _error.value = null
            try {
                // 1. Llama al repositorio para la actualización "en tiempo real"
                val exito = repository.actualizarPuntuacion(
                    partidoId = partidoId,
                    equipo = equipo,
                    nuevoPunto = nuevoPunto
                )

                if (exito) {
                    // 2. Actualización en la lista local para propagar el cambio a la UI
                    _partidosDTO.value = _partidosDTO.value.map { p ->
                        if (p.idPartido == partidoId) {
                            when (equipo) {
                                EquipoType.LOCAL -> p.copy(puntosLocal = nuevoPunto)
                                EquipoType.VISITANTE -> p.copy(puntosVisitante = nuevoPunto)
                            }
                        } else {
                            p
                        }
                    }
                } else {
                    _error.value = "Fallo la actualización de puntuación en el servidor."
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión al actualizar el marcador."
                e.printStackTrace()
            }
        }
    }
}