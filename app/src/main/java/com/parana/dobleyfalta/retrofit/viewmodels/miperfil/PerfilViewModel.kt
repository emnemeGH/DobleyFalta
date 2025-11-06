package com.parana.dobleyfalta.retrofit.viewmodels.miperfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parana.dobleyfalta.retrofit.models.auth.Usuario
import com.parana.dobleyfalta.retrofit.models.usuarios.UsuarioUpdateModel
import com.parana.dobleyfalta.retrofit.repositories.UsuariosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class PerfilViewModel : ViewModel() {

    private val repository = UsuariosRepository()

    private val _usuario = MutableStateFlow<Usuario?>(null)
    val usuario: StateFlow<Usuario?> = _usuario

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun clearError() {
        _error.value = null
    }

    fun cargarUsuario(id: Int) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = repository.obtenerUsuarioPorId(id)
                _usuario.value = response
            } catch (e: Exception) {
                _error.value = "Error al cargar usuario"
            } finally {
                _loading.value = false
            }
        }
    }

    fun actualizarUsuario(id: Int, nombre: String?, correo: String?, nuevaContrasena: String?, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val body = UsuarioUpdateModel(
                    nombre = nombre,
                    correo = correo,
                    nuevaContrasena = nuevaContrasena,
                    rol = null
                )
                val response: Response<Usuario> = repository.actualizarUsuario(id, body)
                if (response.isSuccessful) {
                    _usuario.value = response.body()
                    onSuccess()
                } else if (response.code() == 409) {
                    _error.value = "El correo ya está registrado"
                } else {
                    _error.value = "Error al actualizar"
                }
            } catch (e: Exception) {
                _error.value = "Error al conectar con el servidor"
            } finally {
                _loading.value = false
            }
        }
    }
}