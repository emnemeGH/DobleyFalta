package com.parana.dobleyfalta.retrofit.models.ligas

import java.sql.Date

data class LigaModel(
    val idLiga: Int,
    val nombre: String,
    val fechaInicio: Date,
    val fechaFin: Date,
    val anio: Int
)
