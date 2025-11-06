package com.parana.dobleyfalta.retrofit.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parana.dobleyfalta.retrofit.models.noticia.CrearNoticiaModel
import com.parana.dobleyfalta.retrofit.repositories.NoticiasRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CrearNoticiaViewModel(
    private val repository: NoticiasRepository = NoticiasRepository()
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _success = MutableStateFlow(false)
    val success = _success.asStateFlow()

    fun crearNoticia(noticia: CrearNoticiaModel) {
        viewModelScope.launch {
            try {
                //Se marca que está cargando (loading = true).
                //Se limpia cualquier error anterior (error = null).
                //Se resetea el éxito (success = false).
                _loading.value = true
                _error.value = null
                _success.value = false

                val response = repository.crearNoticia(noticia)

                if (response.isSuccessful) {
                    _success.value = true
                } else {
                    _error.value = "Error ${response.code()}: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Error desconocido"
            } finally {
                _loading.value = false
            }
        }
    }
}