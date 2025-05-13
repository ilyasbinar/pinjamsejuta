package com.example.pinjamsejuta.data.remote.api

import com.example.pinjamsejuta.model.customer.CustomerProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface CustomerApiService {

    @Multipart
    @PUT("update")
    suspend fun updateCustomer(
        @Header("Authorization") token: String,
        @Part ktpPhoto: MultipartBody.Part,
        @Part selfiePhoto: MultipartBody.Part,
        @Part("request") requestJson: RequestBody
    ): Response<String>


    @GET("/api/v1/customer/me")
    fun getCustomerProfile(): Call<CustomerProfileResponse>
}

