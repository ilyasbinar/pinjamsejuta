package com.example.pinjamsejuta.data.remote.api

import com.example.pinjamsejuta.model.loan.PlafondResponse
import retrofit2.Call
import retrofit2.http.GET

interface LoanApiService {
    @GET("plafonds/all")
    fun getPlafonds(): Call<PlafondResponse>
}