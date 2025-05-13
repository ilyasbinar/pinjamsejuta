package com.example.pinjamsejuta.data.repository

import android.content.Context
import com.example.pinjamsejuta.model.customer.CustomerProfileResponse
import com.example.pinjamsejuta.data.remote.api.CustomerApiService
import com.example.pinjamsejuta.data.remote.network.SakuBCAClient
import retrofit2.Call

class CustomerRepository(context: Context) {
    private val api = SakuBCAClient.getInstance(context).create(CustomerApiService::class.java)

    fun getUserProfile(): Call<CustomerProfileResponse> {
        return api.getCustomerProfile()
    }
}
