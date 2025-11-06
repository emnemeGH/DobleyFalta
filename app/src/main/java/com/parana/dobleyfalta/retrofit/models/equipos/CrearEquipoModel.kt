package com.parana.dobleyfalta.retrofit.models.equipos

data class CrearEquipoModel(
    val nombre: String?,
    val ciudad: String?,
    val direccion: String?,
    val logo: String?,
    val descripcion: String?,
    val idLiga: Int?,
    val lat: Double?,
    val lng: Double?
)
