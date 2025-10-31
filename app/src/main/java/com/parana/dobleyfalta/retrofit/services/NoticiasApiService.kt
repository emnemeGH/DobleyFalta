package com.parana.dobleyfalta.retrofit.services

import com.parana.dobleyfalta.retrofit.models.noticia.CrearNoticiaModel
import com.parana.dobleyfalta.retrofit.models.noticia.NoticiaApiModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface NoticiasApiService {

    @GET("api/noticias")
    suspend fun getNoticias(): List<NoticiaApiModel>

    @GET("api/noticias/{id}")
    suspend fun getNoticiaPorId(@Path("id") id: Int): NoticiaApiModel

    @POST("api/noticias")
    suspend fun crearNoticia(@Body nuevaNoticia: CrearNoticiaModel): Response<CrearNoticiaModel>
}