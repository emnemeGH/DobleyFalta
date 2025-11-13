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

      private val _ligaSeleccionada = MutableStateFlow<LigaModel?>(null)
      val ligaSeleccionada = _ligaSeleccionada.asStateFlow()

      // Función AÑADIDA: Limpia el error.
      fun clearError() {
            _error.value = null
          }

      fun cargarLigas() {
            viewModelScope.launch {
                  _loading.value = true
                  try {
                    _ligas.value = repository.obtenerLigas()
                  } catch (e: Exception) {
                    // Modificado para incluir el mensaje de la excepción.
                    _error.value = "Error al obtener ligas: ${e.message}"
                    e.printStackTrace()
                  } finally {
                    _loading.value = false
                  }
                }
          }

      fun obtenerLigaPorId(id: Int) {
            viewModelScope.launch {
                  _loading.value = true
                  try {
                    val liga = repository.getLigaPorId(id)
                    _ligaSeleccionada.value = liga
                  } catch (e: Exception) {
                    _error.value = "Error al obtener liga: ${e.message}"
                    e.printStackTrace()
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
                      // Modificado para capturar el error body si es posible
                      val errorBody = response.errorBody()?.string() ?: response.message()
                      _error.value = "Error al crear liga: ${response.code()} - $errorBody"
                    }
                  } catch (e: Exception) {
                    _error.value = "Error de conexión: ${e.message}"
                    e.printStackTrace()
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
                      val errorBody = response.errorBody()?.string() ?: response.message()
                      _error.value = "Error al actualizar liga: ${response.code()} - $errorBody"
                    }
                  } catch (e: Exception) {
                    _error.value = "Error de conexión: ${e.message}"
                    e.printStackTrace()
                  } finally {
                    _loading.value = false
                  }
                }
          }

      // 🚀 FUNCIÓN AJUSTADA PARA ELIMINAR LIGA
      fun eliminarLiga(id: Int, onSuccess: () -> Unit) {
            viewModelScope.launch {
                  try {
                    _loading.value = true
                    // Asumimos que repository.eliminarLiga(id) devuelve un Response<Unit> o similar
                    val response = repository.eliminarLiga(id)
                   
                    if (response.isSuccessful) {
                      // Recarga la lista para reflejar el cambio
                      cargarLigas()
                      onSuccess()
                    } else {
                      // Captura el mensaje de error del cuerpo o el mensaje por defecto
                      val errorBody = response.errorBody()?.string() ?: response.message()
                      _error.value = "Error al eliminar liga: ${response.code()} - $errorBody"
                    }
                  } catch (e: Exception) {
                    _error.value = "Error de conexión: ${e.message}"
                    e.printStackTrace()
                  } finally {
                    _loading.value = false
                  }
                }
          }
}