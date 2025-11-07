package com.parana.dobleyfalta.retrofit.models.jornadas

import com.parana.dobleyfalta.retrofit.models.ligas.LigaModel

data class JornadaModel(
    val idJornada: Int,
    val numero: Int,
    val fechaInicio: String,
    val fechaFin: String,
    val liga: LigaModel? = null
)

