package com.example.pinjamsejuta.network

import com.example.pinjamsejuta.model.file.FileUploadResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FileUploadService {

    @Multipart
    @POST("api/v1/upload/coba")
    fun uploadFile(
        @Part file: MultipartBody.Part
    ): Call<FileUploadResponse>

    @Multipart
    @POST("api/v1/upload/coba2")
    fun uploadFile2(
        @Part file: MultipartBody.Part,
        @Part file2: MultipartBody.Part
    ): Call<FileUploadResponse>

    //corotine
//    @Multipart
//    @POST("api/v1/upload/coba")
//    suspend fun uploadFile(
//        @Part file: MultipartBody.Part
//    ): Response<FileUploadResponse>
}