package com.example.pinjamsejuta.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pinjamsejuta.network.AuthApiService
import com.example.pinjamsejuta.repository.AuthRepository

class RegisterViewModelFactory(private val apiService: AuthApiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            val repository = AuthRepository(apiService)
            return RegisterViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

