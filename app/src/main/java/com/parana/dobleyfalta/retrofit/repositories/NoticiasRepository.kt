package com.parana.dobleyfalta.retrofit.repositories

import com.parana.dobleyfalta.retrofit.clients.RetrofitClientNoticias
import com.parana.dobleyfalta.retrofit.models.noticia.CrearNoticiaModel
import com.parana.dobleyfalta.retrofit.models.noticia.NoticiaApiModel
import retrofit2.Response

/**
 * Repository = capa intermedia entre la Vista y Retrofit.
 *
 * Su función es encapsular la lógica de red y devolver
 * los datos listos para usar en la interfaz.
 */
class NoticiasRepository {

    private val api = RetrofitClientNoticias.noticiasApiService

    suspend fun obtenerNoticias(): List<NoticiaApiModel> {
        return api.getNoticias()
    }

    suspend fun obtenerNoticiaPorId(id: Int): NoticiaApiModel {
        return api.getNoticiaPorId(id)
    }

    suspend fun actualizarNoticia(id: Int, noticia: CrearNoticiaModel): NoticiaApiModel {
        return api.actualizarNoticia(id, noticia)
    }

    suspend fun crearNoticia(noticia: CrearNoticiaModel): Response<CrearNoticiaModel> {
        return api.crearNoticia(noticia)
    }
}