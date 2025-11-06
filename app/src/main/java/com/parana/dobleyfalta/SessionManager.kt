package com.parana.dobleyfalta

import android.content.Context
import com.google.gson.Gson
import com.parana.dobleyfalta.retrofit.models.auth.Rol
import com.parana.dobleyfalta.retrofit.models.auth.Usuario

class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("session_prefs", Context.MODE_PRIVATE)

    val gson = Gson()

    //JSON no es un objeto, es texto (una cadena de caracteres) que representa un objeto.
    fun saveLogin(token: String, usuarioJson: String) {
        prefs.edit().apply {
            putString("TOKEN", token)
            putString("USUARIO", usuarioJson)
            apply()
        }
    }

    fun getToken(): String? = prefs.getString("TOKEN", null)

    fun getStringUsuario(): String? = prefs.getString("USUARIO", null)

    fun getObjetoUsuario(): Usuario? {
        val usuarioJson = getStringUsuario()
        return if (usuarioJson != null) {
            gson.fromJson(usuarioJson, Usuario::class.java)
        } else {
            null
        }
    }

    fun getRolUsuario(): Rol? {
        return getObjetoUsuario()?.rol
    }

    fun getIdUsuario(): Int? {
        return getObjetoUsuario()?.idUsuario
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}