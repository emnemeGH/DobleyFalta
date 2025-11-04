package com.parana.dobleyfalta.retrofit.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.parana.dobleyfalta.SessionManager
import com.parana.dobleyfalta.retrofit.models.auth.LoginRequest
import com.parana.dobleyfalta.retrofit.models.auth.Rol
import com.parana.dobleyfalta.retrofit.models.auth.Usuario
import com.parana.dobleyfalta.retrofit.repositories.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository()
    private val sessionManager = SessionManager(application.applicationContext)

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    val gson = Gson()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                val response = repository.login(LoginRequest(email, password))

                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!

                    val usuarioJson = gson.toJson(body.usuario)

                    sessionManager.saveLogin(body.token, usuarioJson)
                    onSuccess()
                } else {
                    _error.value = "Credenciales incorrectas"
                }
            } catch (e: Exception) {
                _error.value = "Error al conectar con el servidor"
            } finally {
                _loading.value = false
            }
        }
    }

    fun getUsuarioActual(): Usuario? {
        val usuarioJson = sessionManager.getUsuario()
        return if (usuarioJson != null) {
            gson.fromJson(usuarioJson, Usuario::class.java)
        } else {
            null
        }
    }

    fun getRolUsuario(): Rol? {
        val usuarioJson = sessionManager.getUsuario() ?: return null
        val usuario = gson.fromJson(usuarioJson, Usuario::class.java)
        return usuario.rol
    }

    fun clearError() {
        _error.value = null
    }


}