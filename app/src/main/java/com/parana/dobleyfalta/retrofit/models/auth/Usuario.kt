package com.parana.dobleyfalta.retrofit.models.auth

data class Usuario(
    val idUsuario: Int,
    val correo: String,
    val contrasena: String?,
    val rol: String
)