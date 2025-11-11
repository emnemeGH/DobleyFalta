package com.parana.dobleyfalta.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parana.dobleyfalta.retrofit.clients.RetrofitClientPartidos
import com.parana.dobleyfalta.retrofit.models.partidos.CrearPartidoModel
import com.parana.dobleyfalta.retrofit.models.partidos.PartidoDTOModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CrearPartidoViewModel : ViewModel() {

    private val _estado = MutableStateFlow<String?>(null)
    val estado = _estado.asStateFlow()

    private val _partidoCreado = MutableStateFlow<PartidoDTOModel?>(null)
    val partidoCreado = _partidoCreado.asStateFlow()

    fun crearPartido(partido: CrearPartidoModel) {
        viewModelScope.launch {
            try {
                val response = RetrofitClientPartidos.partidosApiService.crearPartido(partido)
                if (response.isSuccessful) {
                    _partidoCreado.value = response.body()
                    _estado.value = "ok"
                } else {
                    _estado.value = "error: ${response.code()}"
                }
            } catch (e: Exception) {
                _estado.value = "error: ${e.message}"
            }
        }
    }

    fun setError(msg: String) {
        _estado.value = "error: $msg"
    }
}
