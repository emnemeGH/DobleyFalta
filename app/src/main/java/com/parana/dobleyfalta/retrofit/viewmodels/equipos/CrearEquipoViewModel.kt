package com.parana.dobleyfalta.retrofit.viewmodels.equipos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parana.dobleyfalta.retrofit.models.equipos.CrearEquipoModel
import com.parana.dobleyfalta.retrofit.models.equipos.EquipoModel
import com.parana.dobleyfalta.retrofit.repositories.EquiposRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CrearEquipoViewModel : ViewModel() {

    private val repository = EquiposRepository()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _equipoCreado = MutableStateFlow<EquipoModel?>(null)
    val equipoCreado = _equipoCreado.asStateFlow()

    fun crearEquipo(equipo: CrearEquipoModel, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null

                val response = repository.crearEquipo(equipo)

                if (response.isSuccessful) {
                    _equipoCreado.value = response.body()
                    onSuccess()
                } else {
                    _error.value = "Error al crear equipo (${response.code()})"
                }
            } catch (e: Exception) {
                _error.value = "Error al crear equipo"
                e.printStackTrace()
            } finally {
                _loading.value = false
            }
        }
    }
}