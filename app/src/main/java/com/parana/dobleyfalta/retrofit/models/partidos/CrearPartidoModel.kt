package com.parana.dobleyfalta.retrofit.models.partidos

import com.google.gson.annotations.SerializedName

data class CrearPartidoModel(
    @SerializedName("idJornada")
    val idJornada: Int,

    @SerializedName("estadoPartido")
    val estadoPartido: String,

    @SerializedName("idEquipoLocal")
    val idEquipoLocal: Int,

    @SerializedName("idEquipoVisitante")
    val idEquipoVisitante: Int,

    @SerializedName("fecha")
    val fecha: String,

    @SerializedName("puntosLocal")
    val puntosLocal: Int,

    @SerializedName("puntosVisitante")
    val puntosVisitante: Int
)