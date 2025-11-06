package com.parana.dobleyfalta.retrofit.clients

import com.parana.dobleyfalta.retrofit.ApiConstants
import com.parana.dobleyfalta.retrofit.services.EquiposApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientEquipos {
    val equiposApiService: EquiposApiService by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EquiposApiService::class.java)
    }
}