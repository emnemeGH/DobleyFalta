package com.parana.dobleyfalta.retrofit.viewmodels.partidos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parana.dobleyfalta.retrofit.models.partidos.CrearPartidoModel
import com.parana.dobleyfalta.retrofit.models.partidos.PartidoDTOModel
import com.parana.dobleyfalta.retrofit.models.partidos.PartidoModel
import com.parana.dobleyfalta.retrofit.repositories.PartidosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PartidosViewModel : ViewModel() {

    private val repository = PartidosRepository()

    // Si usas PartidoModel (sin equipos embebidos)
    private val _partidos = MutableStateFlow<List<PartidoModel>>(emptyList())
    val partidos = _partidos.asStateFlow()

    // Si quieres trabajar con PartidoDTO (equipos incluidos) podrías usar otra flow:
    private val _partidosDTO = MutableStateFlow<List<PartidoDTOModel>>(emptyList())
    val partidosDTO = _partidosDTO.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun cargarPartidos() {
        viewModelScope.launch {
            _loading.value = true
            try {
                _partidos.value = repository.obtenerPartidos()
                // si tienes endpoint con DTO:
                // _partidosDTO.value = repository.obtenerPartidosConEquipos()
            } catch (e: Exception) {
                _error.value = "Error al obtener partidos: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun crearPartido(partido: CrearPartidoModel, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val resp = repository.crearPartido(partido)
                if (resp.isSuccessful) {
                    cargarPartidos()
                    onSuccess()
                } else {
                    _error.value = "Error al crear partido: ${resp.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión"
            } finally {
                _loading.value = false
            }
        }
    }

    fun actualizarPartido(id: Int, partido: CrearPartidoModel, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val resp = repository.actualizarPartido(id, partido)
                if (resp.isSuccessful) {
                    cargarPartidos()
                    onSuccess()
                } else {
                    _error.value = "Error al actualizar"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión"
            } finally {
                _loading.value = false
            }
        }
    }

    fun eliminarPartido(id: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            try {
                if (repository.eliminarPartido(id)) {
                    cargarPartidos()
                    onSuccess()
                } else {
                    _error.value = "Error al eliminar partido"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión"
            } finally {
                _loading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
