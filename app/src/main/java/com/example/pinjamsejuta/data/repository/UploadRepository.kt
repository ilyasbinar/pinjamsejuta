package com.example.pinjamsejuta.data.repository

import com.example.pinjamsejuta.model.file.FileUploadResponse
import com.example.pinjamsejuta.data.remote.api.FileUploadService
import okhttp3.MultipartBody

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
