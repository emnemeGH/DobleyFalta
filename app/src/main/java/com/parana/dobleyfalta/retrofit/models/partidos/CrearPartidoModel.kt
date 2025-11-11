package com.parana.dobleyfalta.retrofit.models.partidos

data class CrearPartidoModel(
    val idJornada: Int,
    val idEquipoLocal: Int,
    val idEquipoVisitante: Int,
    val fecha: String,
    val estado: String = "proximo"
)
