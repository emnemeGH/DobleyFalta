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

    // Obtenemos la instancia del servicio de noticias desde el client
    private val api = RetrofitClientNoticias.noticiasApiService

    /**
     * Función suspendida → se ejecuta dentro de una coroutine.
     * Llama al endpoint GET /noticias del backend.
     */
    suspend fun obtenerNoticias(): List<NoticiaApiModel> {
        return api.getNoticias()
    }

    suspend fun crearNoticia(noticia: CrearNoticiaModel): Response<CrearNoticiaModel> {
        return api.crearNoticia(noticia)
    }
}