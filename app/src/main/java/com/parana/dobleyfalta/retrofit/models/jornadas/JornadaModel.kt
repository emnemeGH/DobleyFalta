package com.parana.dobleyfalta.retrofit.models.jornadas

import android.os.Parcelable
import com.parana.dobleyfalta.retrofit.models.ligas.LigaModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class JornadaModel(
    val idJornada: Int,
    val numero: Int,
    val fechaInicio: String, // String está bien
    val fechaFin: String,    // String está bien
    val liga: LigaModel? = null // ¡CUIDADO! LigaModel también debe ser Parcelable
) : Parcelable

