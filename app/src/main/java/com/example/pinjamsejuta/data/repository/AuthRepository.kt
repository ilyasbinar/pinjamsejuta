package com.example.pinjamsejuta.data.repository

import com.example.pinjamsejuta.model.auth.LoginRequest
import com.example.pinjamsejuta.model.auth.LoginResponse
import com.example.pinjamsejuta.model.auth.RegisterRequest
import com.example.pinjamsejuta.model.auth.RegisterResponse
import com.example.pinjamsejuta.data.remote.api.AuthApiService
import retrofit2.Call
import retrofit2.Response

class AuthRepository(private val api: AuthApiService) {
    fun registerUser(request: RegisterRequest): Call<RegisterResponse> {
        return api.registerCustomer(request)
    }

    fun loginCustomer(request: LoginRequest): Call<LoginResponse> {
        return api.loginCustomer(request)
    }

    suspend fun loginWithGoogle(idToken: String): Response<LoginResponse> {
        return api.loginWithGoogle(idToken)
    }
}
