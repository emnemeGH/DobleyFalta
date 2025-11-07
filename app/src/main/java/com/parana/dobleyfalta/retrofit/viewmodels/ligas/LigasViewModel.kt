package com.parana.dobleyfalta.retrofit.viewmodels.ligas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parana.dobleyfalta.retrofit.models.ligas.CrearLigaModel
import com.parana.dobleyfalta.retrofit.models.ligas.LigaModel
import com.parana.dobleyfalta.retrofit.models.ligas.LigaUpdateModel
import com.parana.dobleyfalta.retrofit.repositories.LigasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LigasViewModel : ViewModel() {

    private val repository = LigasRepository()

    private val _ligas = MutableStateFlow<List<LigaModel>>(emptyList())
    val ligas = _ligas.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun cargarLigas() {
        viewModelScope.launch {
            _loading.value = true
            try {
                _ligas.value = repository.obtenerLigas()
            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = "Error al obtener ligas: {e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun crearLiga(liga: CrearLigaModel, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val response = repository.crearLiga(liga)
                if (response.isSuccessful) {
                    cargarLigas()
                    onSuccess()
                } else {
                    _error.value = "Error al crear liga: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión"
            } finally {
                _loading.value = false
            }
        }
    }

    fun actualizarLiga(id: Int, liga: LigaUpdateModel, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val response = repository.actualizarLiga(id, liga)
                if (response.isSuccessful) {
                    cargarLigas()
                    onSuccess()
                } else {
                    _error.value = "Error al actualizar liga"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión"
            } finally {
                _loading.value = false
            }
        }
    }

    fun eliminarLiga(id: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                _loading.value = true
                if (repository.eliminarLiga(id)) {
                    cargarLigas()
                    onSuccess()
                } else {
                    _error.value = "Error al eliminar liga"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión"
            } finally {
                _loading.value = false
            }
        }
    }
}
