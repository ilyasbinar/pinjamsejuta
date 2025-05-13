package com.example.pinjamsejuta.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pinjamsejuta.data.remote.api.LoanApiService
import com.example.pinjamsejuta.data.remote.network.SakuBCAClient
import com.example.pinjamsejuta.data.repository.LoanMasterRepository

class HomeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val apiService = SakuBCAClient.getInstance(context).create(LoanApiService::class.java)
        val repository = LoanMasterRepository(apiService)
        return HomeViewModel(repository) as T
    }
}
