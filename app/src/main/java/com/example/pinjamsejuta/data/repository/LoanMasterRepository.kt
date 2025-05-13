package com.example.pinjamsejuta.data.repository

import com.example.pinjamsejuta.data.remote.api.LoanApiService
import com.example.pinjamsejuta.model.loan.PlafondResponse
import retrofit2.Call

class LoanMasterRepository(private val apiService: LoanApiService) {

    fun getPlafonds(): Call<PlafondResponse> {
        return apiService.getPlafonds()
    }
}
