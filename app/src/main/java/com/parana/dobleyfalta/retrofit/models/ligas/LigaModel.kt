package com.parana.dobleyfalta.retrofit.models.ligas

import android.os.Parcelable
import com.parana.dobleyfalta.retrofit.models.jornadas.JornadaModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class LigaModel(
    val idLiga: Int,
    val nombre: String,
    val fechaInicio: String,
    val fechaFin: String,
    val anio: Int,
    val jornadas: List<JornadaModel> = emptyList()
) : Parcelable
