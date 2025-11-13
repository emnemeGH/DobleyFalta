package com.parana.dobleyfalta.retrofit.viewmodels.partidos

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parana.dobleyfalta.retrofit.models.equipos.EquipoModel
import com.parana.dobleyfalta.retrofit.models.partidos.EditarPartidoModel
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

    fun actualizarPuntuacionIncremental(
        partidoId: Int,
        equipo: EquipoType,
        incremento: Int
    ) {
        // Obtenemos la lista actual
        val listaActual = _partidosDTO.value.toMutableList()

        // Buscamos el índice del partido
        val index = listaActual.indexOfFirst { it.idPartido == partidoId }

        if (index != -1) {
            val partidoActual = listaActual[index]

            // Calculamos el nuevo puntaje total
            val nuevoPuntajeTotal = when (equipo) {
                EquipoType.LOCAL -> (partidoActual.puntosLocal ?: 0) + incremento
                EquipoType.VISITANTE -> (partidoActual.puntosVisitante ?: 0) + incremento
            }

            // Aseguramos que el puntaje no sea negativo
            val puntajeFinal = maxOf(0, nuevoPuntajeTotal)

            // Creamos una COPIA INMUTABLE con el nuevo puntaje
            val partidoActualizado = when (equipo) {
                EquipoType.LOCAL -> partidoActual.copy(puntosLocal = puntajeFinal)
                EquipoType.VISITANTE -> partidoActual.copy(puntosVisitante = puntajeFinal)
            }

            // Reemplazamos el partido en la lista
            listaActual[index] = partidoActualizado

            // Emitimos la nueva lista al StateFlow, lo que actualiza la UI al instante
            _partidosDTO.value = listaActual
        }
    }

    fun guardarPartido(partidoId: Int, puntosLocal: Int, puntosVisitante: Int, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            var exitoLocal = false
            var exitoVisitante = false

            try {
                // 💡 CORRECCIÓN: Usar EquipoType.LOCAL en lugar del String "LOCAL"
                exitoLocal = repository.actualizarPuntuacionBackend(
                    partidoId,
                    EquipoType.LOCAL,
                    puntosLocal
                )

                // 💡 CORRECCIÓN: Usar EquipoType.VISITANTE en lugar del String "VISITANTE"
                exitoVisitante = repository.actualizarPuntuacionBackend(
                    partidoId,
                    EquipoType.VISITANTE,
                    puntosVisitante
                )

                // Informar a la UI si AMBAS fueron exitosas
                onComplete(exitoLocal && exitoVisitante)

            } catch (e: Exception) {
                _error.value = "Error al conectar para guardar el partido $partidoId: ${e.message}"
                e.printStackTrace()
                onComplete(false)
            }
        }
    }

    fun updatePartido(
        partidoId: Int,
        idJornada: Int,
        estadoPartido: String,
        idEquipoLocal: Int,
        idEquipoVisitante: Int,
        fecha: String,
        onComplete: (Boolean) -> Unit
    ) {
        viewModelScope.launch { // Uso de Coroutines para tarea asíncrona
            _loading.value = true
            _error.value = null
            try {
                // 1. Crear el modelo de datos para la solicitud PUT
                val partidoUpdate = EditarPartidoModel(
                    idJornada = idJornada,
                    estadoPartido = estadoPartido,
                    idEquipoLocal = idEquipoLocal,
                    idEquipoVisitante = idEquipoVisitante,
                    fecha = fecha
                )

                // 2. Llamada al Repositorio (Capa de Datos)
                val response = repository.editarPartido(partidoId, partidoUpdate)

                if (response.isSuccessful && response.body() != null) {
                    val partidoModelActualizado = response.body()!!

                    // 3. Asegurar la caché de equipos y mapear el resultado
                    if (equipos.isEmpty()) {
                        equipos = equiposRepository.obtenerEquipos()
                    }

                    // Recreamos el DTO actualizado con los nombres/logos de equipo
                    val equipoLocal = equipos.find { it.idEquipo == partidoModelActualizado.idEquipoLocal }
                    val equipoVisitante = equipos.find { it.idEquipo == partidoModelActualizado.idEquipoVisitante }

                    val partidoDTOActualizado = PartidoDTOModel(
                        idPartido = partidoModelActualizado.idPartido,
                        fecha = partidoModelActualizado.fecha,
                        puntosLocal = partidoModelActualizado.puntosLocal,
                        puntosVisitante = partidoModelActualizado.puntosVisitante,
                        jornadaId = partidoModelActualizado.jornada?.idJornada ?: 0,
                        jornadaNumero = partidoModelActualizado.jornada?.numero ?: 0,
                        estadoPartido = partidoModelActualizado.estadoPartido,
                        equipoLocalId = partidoModelActualizado.idEquipoLocal ?: 0,
                        equipoVisitanteId = partidoModelActualizado.idEquipoVisitante ?: 0,
                        equipoLocalNombre = equipoLocal?.nombre,
                        equipoVisitanteNombre = equipoVisitante?.nombre,
                        logoLocal = equipoLocal?.logo,
                        logoVisitante = equipoVisitante?.logo
                    )

                    // 4. Actualizar el StateFlow (Single Source of Truth) para refrescar la UI
                    val listaActual = _partidosDTO.value.toMutableList()
                    val index = listaActual.indexOfFirst { it.idPartido == partidoId }

                    if (index != -1) {
                        listaActual[index] = partidoDTOActualizado
                        _partidosDTO.value = listaActual // Emitir la nueva lista inmutable
                    }

                    onComplete(true) // Notificar éxito

                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Error al editar el partido."
                    _error.value = "Error al editar el partido. Código: ${response.code()}: $errorMsg"
                    onComplete(false)
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión al editar el partido $partidoId: ${e.message}"
                Log.e("PartidosViewModel", "Error al editar partido", e)
                onComplete(false)
            } finally {
                _loading.value = false
            }
        }
    }
}
