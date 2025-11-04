package com.parana.dobleyfalta.retrofit.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parana.dobleyfalta.retrofit.models.auth.Rol
import com.parana.dobleyfalta.retrofit.models.usuarios.CrearUsuarioModel
import com.parana.dobleyfalta.retrofit.repositories.UsuariosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegistroViewModel : ViewModel() {
    private val repository = UsuariosRepository()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun clearError() {
        _error.value = null
    }

    fun registrarUsuario(
        nombre: String,
        correo: String,
        contrasena: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val nuevoUsuario = CrearUsuarioModel(
                    nombre = nombre,
                    correo = correo,
                    contrasena = contrasena,
                    rol = Rol.Registrado
                )

                val response = repository.crearUsuario(nuevoUsuario)

                if (response.isSuccessful) {
                    onSuccess()
                } else if (response.code() == 409) {
                    _error.value = "El correo ya está registrado"
                } else {
                    _error.value = "Error al registrar: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión"
            } finally {
                _loading.value = false
            }
        }
    }
}