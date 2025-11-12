package com.parana.dobleyfalta.retrofit.models.jornadas

import com.google.gson.annotations.SerializedName

data class JornadaIdModel(
    // La propiedad dentro de la entidad 'Jornada' en el backend es 'idJornada'
    @SerializedName("idJornada")
    val id: Int
)