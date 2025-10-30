package com.parana.dobleyfalta.retrofit.clients

import com.parana.dobleyfalta.retrofit.ApiConstants
import com.parana.dobleyfalta.retrofit.services.NoticiasApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientNoticias {
    val noticiasApiService: NoticiasApiService by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL_NOTICIAS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NoticiasApiService::class.java)
    }
}