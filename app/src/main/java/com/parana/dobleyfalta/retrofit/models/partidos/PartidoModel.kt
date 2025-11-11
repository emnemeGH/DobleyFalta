package com.parana.dobleyfalta.retrofit.models.partidos

import com.parana.dobleyfalta.retrofit.models.jornadas.JornadaModel

data class PartidoModel(
    val idPartido: Int,
    val fecha: String?,
    val puntosLocal: Int?,
    val puntosVisitante: Int?,
    val idEquipoLocal: Int?,
    val idEquipoVisitante: Int?,
    val jornada: JornadaModel?,
    val estadoPartido: String
)