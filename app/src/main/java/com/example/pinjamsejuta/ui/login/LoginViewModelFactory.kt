package com.example.pinjamsejuta.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pinjamsejuta.network.AuthApiService
import com.example.pinjamsejuta.repository.AuthRepository

class LoginViewModelFactory(private val apiService: AuthApiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            val repository = AuthRepository(apiService)
            return LoginViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

