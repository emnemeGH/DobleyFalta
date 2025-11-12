package com.parana.dobleyfalta.retrofit.viewmodels.tablas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.parana.dobleyfalta.retrofit.models.tablas.TablaDTOModel
import com.parana.dobleyfalta.retrofit.repositories.TablasRepository
import kotlinx.coroutines.launch

class TablaViewModel : ViewModel() {

    private val repository = TablasRepository()

    private val _tabla = MutableLiveData<List<TablaDTOModel>>()
    val tabla: LiveData<List<TablaDTOModel>> get() = _tabla

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun cargarTablaPorLiga(idLiga: Int) {
        viewModelScope.launch {
            try {
                val resultado = repository.obtenerTablaPorLiga(idLiga)
                _tabla.value = resultado
            } catch (e: Exception) {
                _error.value = "Error al obtener la tabla: ${e.message}"
            }
        }
    }

}
