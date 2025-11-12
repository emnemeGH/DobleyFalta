package com.parana.dobleyfalta.retrofit.models.partidos

import android.os.Parcelable
import com.parana.dobleyfalta.retrofit.models.jornadas.JornadaModel
import kotlinx.parcelize.Parcelize

@Parcelize // <--- ¡Esto resuelve el problema!
data class PartidoModel(
    val idPartido: Int,
    // Asegúrate de que todas las propiedades aquí dentro también sean Parcelable o tipos base
    val fecha: String, // Asumiendo que es String en el modelo de respuesta
    val puntosLocal: Int,
    val puntosVisitante: Int,
    val idEquipoLocal: Int,
    val idEquipoVisitante: Int,
    // Si tienes objetos anidados como JornadaModel, también deben ser Parcelable
    val jornada: JornadaModel? = null, // Usa tu modelo de jornada aquí
    val estadoPartido: String // O el Enum si lo manejas como Enum en el modelo de respuesta
) : Parcelable