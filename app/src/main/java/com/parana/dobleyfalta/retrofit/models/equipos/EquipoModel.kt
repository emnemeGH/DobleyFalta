package com.parana.dobleyfalta.retrofit.models.equipos

data class EquipoModel(
    val idEquipo: Int,
    val nombre: String,
    val ciudad: String,
    val direccion: String,
    val logo: String,
    val descripcion: String,
    val idLiga: Int,
    val lat: Double,
    val lng: Double
)
