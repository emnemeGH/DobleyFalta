package com.parana.dobleyfalta.retrofit.repositories

import com.parana.dobleyfalta.retrofit.clients.RetrofitClientUsuarios
import com.parana.dobleyfalta.retrofit.models.auth.Usuario
import com.parana.dobleyfalta.retrofit.models.usuarios.CrearUsuarioModel
import com.parana.dobleyfalta.retrofit.models.usuarios.UsuarioUpdateModel
import retrofit2.Response

class UsuariosRepository {

    private val api = RetrofitClientUsuarios.usuariosApiService

    suspend fun obtenerUsuarios(): List<Usuario> {
        return api.getUsuarios()
    }

    suspend fun obtenerUsuarioPorId(id: Int): Usuario {
        return api.getUsuarioPorId(id)
    }

    suspend fun crearUsuario(usuario: CrearUsuarioModel): Response<Usuario> {
        return api.crearUsuario(usuario)
    }

    suspend fun actualizarUsuario(id: Int, usuario: UsuarioUpdateModel): Response<Usuario> {
        return api.actualizarUsuario(id, usuario)
    }

    suspend fun eliminarUsuario(id: Int): Boolean {
        val response = api.eliminarUsuario(id)
        return response.isSuccessful
    }
}