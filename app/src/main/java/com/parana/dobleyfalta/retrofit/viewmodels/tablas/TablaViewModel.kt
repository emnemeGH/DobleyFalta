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

    private val _tablas = MutableStateFlow<Map<Int, List<TablaDTOModel>>>(emptyMap())
    val tablas = _tablas.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun cargarTablaPorLiga(idLiga: Int) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val tabla = repository.obtenerTablaPorLiga(idLiga)
                _tablas.value = _tablas.value + (idLiga to tabla)
            } catch (e: Exception) {
                _error.value = "Error al obtener tabla de la liga $idLiga: ${e.message}"
                e.printStackTrace()
            } finally {
                _loading.value = false
            }
        }
    }

}
