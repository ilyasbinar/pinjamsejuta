package com.example.pinjamsejuta.data.remote.api

import com.example.pinjamsejuta.model.auth.LoginRequest
import com.example.pinjamsejuta.model.auth.LoginResponse
import com.example.pinjamsejuta.model.auth.RegisterRequest
import com.example.pinjamsejuta.model.auth.RegisterResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/register/customer")
    fun registerCustomer(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("auth/login-customer")
    fun loginCustomer(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @FormUrlEncoded
    @POST("api/auth/google")
    suspend fun loginWithGoogle(
        @Field("idToken") idToken: String
    ): Response<LoginResponse>

}
