package com.parana.dobleyfalta.retrofit.clients

import com.parana.dobleyfalta.retrofit.ApiConstants
import com.parana.dobleyfalta.retrofit.services.ProductosApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientProductos {

    val productosApiService: ProductosApiService by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductosApiService::class.java)
    }

}