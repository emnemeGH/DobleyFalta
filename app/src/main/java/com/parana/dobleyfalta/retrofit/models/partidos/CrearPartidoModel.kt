package com.parana.dobleyfalta.retrofit.models.partidos

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class CrearPartidoModel(
    // ✅ 1. ¡ID SIMPLE RESUELTO! Mapea a 'idJornada' del DTO en el backend
    @SerializedName("idJornada")
    val idJornada: Int,

    // ✅ 2. Solución para el campo NOT NULL
    @SerializedName("estadoPartido")
    val estadoPartido: String = "proximo",

    // El resto de campos (asegurando camelCase para el DTO)
    @SerializedName("idEquipoLocal")
    val idEquipoLocal: Int,

    @SerializedName("idEquipoVisitante")
    val idEquipoVisitante: Int,

    @SerializedName("fecha")
    val fecha: String,

    @SerializedName("puntosLocal")
    val puntosLocal: Int = 0,

    @SerializedName("puntosVisitante")
    val puntosVisitante: Int = 0
)