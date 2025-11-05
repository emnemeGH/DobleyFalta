package com.parana.dobleyfalta.retrofit.models.partidos

data class EquipoDTOModel(
    val idEquipo: Int?,
    val nombre: String?,
    val logo: String?    // puede ser URL o nombre de recurso; depende de tu backend
)
