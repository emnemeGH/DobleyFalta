package com.parana.dobleyfalta.retrofit.clients

import android.content.Context
import com.parana.dobleyfalta.SessionManager
import com.parana.dobleyfalta.retrofit.ApiConstants
import com.parana.dobleyfalta.retrofit.interceptors.AuthInterceptor
import com.parana.dobleyfalta.retrofit.services.NoticiasApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientNoticias {
    private var retrofit: Retrofit? = null

    fun init(context: Context) {
        val sessionManager = SessionManager(context.applicationContext)
        val authInterceptor = AuthInterceptor(sessionManager)

        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val noticiasApiService: NoticiasApiService
        get() = retrofit?.create(NoticiasApiService::class.java)
            ?: throw IllegalStateException("RetrofitClientNoticias not initialized")
}
