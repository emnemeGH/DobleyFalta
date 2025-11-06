package com.parana.dobleyfalta.retrofit.repositories

import com.parana.dobleyfalta.retrofit.clients.RetrofitClientAuth
import com.parana.dobleyfalta.retrofit.models.auth.LoginRequest
import com.parana.dobleyfalta.retrofit.models.auth.LoginResponse
import retrofit2.Response

class AuthRepository {

    private val api = RetrofitClientAuth.authApiService

    suspend fun login(request: LoginRequest): Response<LoginResponse> {
        return api.login(request)
    }
}