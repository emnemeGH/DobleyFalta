package com.parana.dobleyfalta.retrofit.models.jornadas

import java.sql.Date

data class JornadaModel(
    val idJornada: Int,
    val numero: Int,
    val fechaInicio: Date,
    val fechaFin: Date,
    val liga: LigaModel? = null
) {
    annotation class LigaModel
}
