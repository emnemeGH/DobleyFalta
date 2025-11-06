package com.parana.dobleyfalta.retrofit.models.usuarios

import com.parana.dobleyfalta.retrofit.models.auth.Rol

data class CrearUsuarioModel(
    val nombre: String,
    val correo: String,
    val contrasena: String,
    val rol: Rol
)