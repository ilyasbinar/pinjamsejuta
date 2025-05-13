package com.example.pinjamsejuta.ui.account

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pinjamsejuta.data.repository.CustomerRepository

class AccountViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountViewModel::class.java)) {
            return AccountViewModel(CustomerRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
