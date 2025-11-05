package com.parana.dobleyfalta.retrofit.models.partidos

import java.sql.Date

data class PartidoModel(
    val idPartido: Int,
    val fecha: String?,                 // backend usa java.sql.Date -> se recibe como String (ISO) o "yyyy-MM-dd"
    val puntosLocal: Int?,
    val puntosVisitante: Int?,
    val idEquipoLocal: Int?,
    val idEquipoVisitante: Int?,
    val idJornada: Int?
)
