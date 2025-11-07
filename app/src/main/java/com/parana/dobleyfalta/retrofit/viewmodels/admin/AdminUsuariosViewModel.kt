package com.parana.dobleyfalta.retrofit.viewmodels.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parana.dobleyfalta.retrofit.models.auth.Usuario
import com.parana.dobleyfalta.retrofit.repositories.UsuariosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminUsuariosViewModel : ViewModel() {
    private val repository = UsuariosRepository()

    private val _usuarios = MutableStateFlow<List<Usuario>>(emptyList())
    val usuarios: StateFlow<List<Usuario>> = _usuarios

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun cargarUsuarios() {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null
                _usuarios.value = repository.obtenerUsuarios()
            } catch (e: Exception) {
                _error.value = "Error al cargar los usuarios"
                e.printStackTrace()
            } finally {
                _loading.value = false
            }
        }
    }
}