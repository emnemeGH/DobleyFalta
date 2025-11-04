package com.parana.dobleyfalta.retrofit.services

import com.parana.dobleyfalta.retrofit.models.auth.LoginRequest
import com.parana.dobleyfalta.retrofit.models.auth.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}