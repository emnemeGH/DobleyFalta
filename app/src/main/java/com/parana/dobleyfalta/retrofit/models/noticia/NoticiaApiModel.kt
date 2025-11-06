package com.parana.dobleyfalta.retrofit.models.noticia

data class NoticiaApiModel(
    val idNoticia: Int,
    val titulo: String,
    val contenido: String,
    val fechaPublicacion: String,
    val imagen: String
)