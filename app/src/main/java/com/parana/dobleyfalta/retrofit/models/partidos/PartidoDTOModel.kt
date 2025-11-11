package com.parana.dobleyfalta.retrofit.models.partidos

data class PartidoDTOModel(
    val idPartido: Int,
    val fecha: String?,
    val puntosLocal: Int?,
    val puntosVisitante: Int?,
    val jornadaId: Int,
    val jornadaNumero: Int,
    val estadoPartido: String?,
    val equipoLocalId: Int,
    val equipoVisitanteId: Int,
    val equipoLocalNombre: String?,
    val equipoVisitanteNombre: String?,
    val logoLocal: String?,
    val logoVisitante: String?
)
