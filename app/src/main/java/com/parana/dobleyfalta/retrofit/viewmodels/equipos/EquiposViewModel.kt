package com.parana.dobleyfalta.retrofit.viewmodels.equipos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parana.dobleyfalta.retrofit.models.equipos.EquipoModel
import com.parana.dobleyfalta.retrofit.repositories.EquiposRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EquiposViewModel : ViewModel() {

    private val repository = EquiposRepository()

    private val _equipos = MutableStateFlow<List<EquipoModel>>(emptyList())
    val equipos = _equipos.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun cargarEquipos() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                _equipos.value = repository.obtenerEquipos()
            } catch (e: Exception) {
                _error.value = "Error al cargar equipos: ${e.message}"
                e.printStackTrace()
            } finally {
                _loading.value = false
            }
        }
    }

    fun eliminarEquipo(id: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val exito = repository.eliminarEquipo(id)
                if (exito) {
                    _equipos.value = _equipos.value.filter { it.idEquipo != id }
                    onSuccess()
                } else {
                    _error.value = "Error al eliminar el equipo"
                }
            } catch (e: Exception) {
                _error.value = "Error al eliminar equipo: ${e.message}"
                e.printStackTrace()
            }
        }
    }
}