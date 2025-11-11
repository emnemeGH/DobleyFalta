package com.parana.dobleyfalta.retrofit.models.partidos

import com.google.gson.annotations.SerializedName

data class MarcadorUpdateRequest(
    @SerializedName("equipo")
    val equipo: String, // Valor será "LOCAL" o "VISITANTE"
    @SerializedName("puntos")
    val puntos: Int // El nuevo puntaje total
)
