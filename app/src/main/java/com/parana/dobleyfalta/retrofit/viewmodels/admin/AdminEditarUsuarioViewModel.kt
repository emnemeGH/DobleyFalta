package com.parana.dobleyfalta.retrofit.viewmodels.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parana.dobleyfalta.retrofit.models.auth.Usuario
import com.parana.dobleyfalta.retrofit.models.usuarios.UsuarioUpdateModel
import com.parana.dobleyfalta.retrofit.repositories.UsuariosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminEditarUsuarioViewModel : ViewModel() {
    private val repository = UsuariosRepository()

    private val _usuario = MutableStateFlow<Usuario?>(null)
    val usuario = _usuario.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun cargarUsuario(id: Int) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null
                val usuario = repository.obtenerUsuarioPorId(id)
                _usuario.value = usuario
            } catch (e: Exception) {
                _error.value = "Error al cargar el usuario"
            } finally {
                _loading.value = false
            }
        }
    }

    fun actualizarUsuario(
        id: Int,
        nombre: String,
        correo: String,
        nuevaContrasena: String,
        rol: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val dto = UsuarioUpdateModel(
                    nombre = nombre,
                    correo = correo,
                    nuevaContrasena = if (nuevaContrasena.isNotEmpty()) nuevaContrasena else null,
                    rol = rol
                )
                repository.actualizarUsuario(id, dto)
                onSuccess()
            } catch (e: Exception) {
                _error.value = "Error al actualizar: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
}