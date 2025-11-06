package com.parana.dobleyfalta.retrofit.models.usuarios

data class UsuarioUpdateModel(
    val nombre: String?,
    val correo: String?,
    val nuevaContrasena: String?,
    val rol: String?
)