package com.example.pinjamsejuta.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
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
}
