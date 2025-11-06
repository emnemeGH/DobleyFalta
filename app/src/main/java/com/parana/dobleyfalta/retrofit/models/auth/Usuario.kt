package com.parana.dobleyfalta.retrofit.models.auth

data class Usuario(
    val idUsuario: Int,
    val nombre: String,
    val correo: String,
    val contrasena: String?,
    val rol: Rol
)