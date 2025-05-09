package com.example.pinjamsejuta.repository

import com.example.pinjamsejuta.model.file.FileUploadResponse
import com.example.pinjamsejuta.network.FileUploadService
import okhttp3.MultipartBody
import retrofit2.Call

class UploadRepository(private val fileUploadService: FileUploadService) {
    fun uploadFile(filePart: MultipartBody.Part, callback: retrofit2.Callback<FileUploadResponse>) {
        fileUploadService.uploadFile(filePart).enqueue(callback)
    }
    fun uploadFile2(filePart: MultipartBody.Part, filePart2: MultipartBody.Part, callback: retrofit2.Callback<FileUploadResponse>) {
        fileUploadService.uploadFile2(filePart, filePart2).enqueue(callback)
    }

    //untuk corotine
//    suspend fun uploadFile(filePart: MultipartBody.Part): Response<FileUploadResponse> {
//        return apiService.uploadFile(filePart)
//    }
}
