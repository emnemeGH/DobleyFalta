package com.parana.dobleyfalta.retrofit.models.jornadas

import java.sql.Date

data class CrearJornadaModel(
    val numero: Int,
    val fechaInicio: Date,
    val fechaFin: Date,
    val idLiga: Int
)
