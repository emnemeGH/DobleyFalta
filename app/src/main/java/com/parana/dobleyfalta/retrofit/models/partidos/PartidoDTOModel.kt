package com.parana.dobleyfalta.retrofit.models.partidos

data class PartidoDTOModel(
    val idPartido: Int?,
    val fecha: String?,
    val puntosLocal: Int?,
    val puntosVisitante: Int?,
    val equipoLocal: EquipoDTOModel?,
    val equipoVisitante: EquipoDTOModel?
)
