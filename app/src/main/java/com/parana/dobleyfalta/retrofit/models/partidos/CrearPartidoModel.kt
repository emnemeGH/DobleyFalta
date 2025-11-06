package com.parana.dobleyfalta.retrofit.models.partidos

data class CrearPartidoModel(
    val fecha: String,          // formato "yyyy-MM-dd" o lo que tu backend acepte
    val puntosLocal: Int?,
    val puntosVisitante: Int?,
    val idEquipoLocal: Int,
    val idEquipoVisitante: Int,
    val idJornada: Int?
)
