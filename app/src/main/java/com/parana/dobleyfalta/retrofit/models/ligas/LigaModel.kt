package com.parana.dobleyfalta.retrofit.models.ligas

import com.parana.dobleyfalta.retrofit.models.jornadas.JornadaModel

data class LigaModel(
    val idLiga: Int,
    val nombre: String,
    val fechaInicio: String,
    val fechaFin: String,
    val anio: Int,
    val jornadas: List<JornadaModel> = emptyList()
)
