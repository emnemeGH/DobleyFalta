package com.parana.dobleyfalta.retrofit.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parana.dobleyfalta.retrofit.models.jornadas.CrearJornadaModel
import com.parana.dobleyfalta.retrofit.models.jornadas.JornadaModel
import com.parana.dobleyfalta.retrofit.repositories.JornadasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class JornadasViewModel : ViewModel() {

    private val repository = JornadasRepository()

    private val _jornadas = MutableStateFlow<List<JornadaModel>>(emptyList())
    val jornadas = _jornadas.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun clearError() {
        _error.value = null
    }

    fun obtenerJornadas() {
        viewModelScope.launch {
            try {
                _loading.value = true
                _jornadas.value = repository.obtenerJornadas()
            } catch (e: Exception) {
                _error.value = "Error al obtener jornadas: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun crearJornada(jornada: CrearJornadaModel, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val response = repository.crearJornada(jornada)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    _error.value = "Error al crear jornada (${response.code()})"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun editarJornada(id: Int, jornada: CrearJornadaModel, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val response = repository.editarJornada(id, jornada)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    _error.value = "Error al editar jornada (${response.code()})"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun eliminarJornada(id: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val ok = repository.eliminarJornada(id)
                if (ok) onSuccess()
                else _error.value = "Error al eliminar jornada"
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
}
