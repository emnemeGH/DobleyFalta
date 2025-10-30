package com.parana.dobleyfalta.retrofit.services

import com.parana.dobleyfalta.retrofit.models.noticia.CrearNoticiaModel
import com.parana.dobleyfalta.retrofit.models.noticia.NoticiaApiModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NoticiasApiService {

    @GET("noticias")
    suspend fun getNoticias(): List<NoticiaApiModel>


    @POST("noticias")
    suspend fun crearNoticia(@Body nuevaNoticia: CrearNoticiaModel): Response<CrearNoticiaModel>
}