package com.parana.dobleyfalta.retrofit.clients

import com.parana.dobleyfalta.retrofit.ApiConstants
import com.parana.dobleyfalta.retrofit.services.UsuariosApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientUsuarios {
    val usuariosApiService: UsuariosApiService by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UsuariosApiService::class.java)
    }
}