package com.parana.dobleyfalta.retrofit.clients

import com.parana.dobleyfalta.retrofit.ApiConstants
import com.parana.dobleyfalta.retrofit.services.AuthApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientAuth {
    val authApiService: AuthApiService by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApiService::class.java)
    }
}