package com.parana.dobleyfalta

import android.content.Context

class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("session_prefs", Context.MODE_PRIVATE)


    fun saveLogin(token: String, usuarioJson: String) {
        prefs.edit().apply {
            putString("TOKEN", token)
            putString("USUARIO", usuarioJson)
            apply()
        }
    }

    fun getToken(): String? = prefs.getString("TOKEN", null)

    fun getUsuario(): String? = prefs.getString("USUARIO", null)

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}