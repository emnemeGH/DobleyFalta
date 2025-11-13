package com.parana.dobleyfalta.retrofit.models.partidos

import android.os.Parcelable
import com.parana.dobleyfalta.retrofit.models.jornadas.JornadaModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class PartidoModel(
    val idPartido: Int,
    val fecha: String,
    val puntosLocal: Int,
    val puntosVisitante: Int,
    val idEquipoLocal: Int,
    val idEquipoVisitante: Int,
    val jornada: JornadaModel? = null, // Usa tu modelo de jornada aquí
    val estadoPartido: String // O el Enum si lo manejas como Enum en el modelo de respuesta
) : Parcelable