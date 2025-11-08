package com.parana.dobleyfalta.retrofit.viewmodels.equipos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parana.dobleyfalta.retrofit.models.equipos.CrearEquipoModel
import com.parana.dobleyfalta.retrofit.models.equipos.EquipoModel
import com.parana.dobleyfalta.retrofit.repositories.EquiposRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditarEquipoViewModel : ViewModel() {
    private val repository = EquiposRepository()

    private val _equipo = MutableStateFlow<EquipoModel?>(null)
    val equipo: StateFlow<EquipoModel?> = _equipo

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun cargarEquipo(id: Int) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                _equipo.value = repository.obtenerEquipoPorId(id)
            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = "Error al cargar equipo"
            } finally {
                _loading.value = false
            }
        }
    }

    fun actualizarEquipo(id: Int, equipo: CrearEquipoModel, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                repository.actualizarEquipo(id, equipo)
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = "Error al actualizar el equipo"
            } finally {
                _loading.value = false
            }
        }
    }
}