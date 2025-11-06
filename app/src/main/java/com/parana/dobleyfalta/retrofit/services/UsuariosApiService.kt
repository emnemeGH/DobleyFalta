package com.parana.dobleyfalta.retrofit.services

import com.parana.dobleyfalta.retrofit.models.auth.Usuario
import com.parana.dobleyfalta.retrofit.models.usuarios.CrearUsuarioModel
import com.parana.dobleyfalta.retrofit.models.usuarios.UsuarioUpdateModel
import retrofit2.Response
import retrofit2.http.*

interface UsuariosApiService {

    @GET("api/usuarios/all")
    suspend fun getUsuarios(): List<Usuario>

    @GET("api/usuarios/{id}")
    suspend fun getUsuarioPorId(@Path("id") id: Int): Usuario

    @POST("api/usuarios")
    suspend fun crearUsuario(@Body usuario: CrearUsuarioModel): Response<Usuario>

    @PUT("api/usuarios/{id}")
    suspend fun actualizarUsuario(
        @Path("id") id: Int,
        @Body usuario: UsuarioUpdateModel
    ): Response<Usuario>

    @DELETE("api/usuarios/{id}")
    suspend fun eliminarUsuario(@Path("id") id: Int): Response<Unit>
}