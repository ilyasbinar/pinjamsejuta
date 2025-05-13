package com.example.pinjamsejuta.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pinjamsejuta.model.customer.CustomerProfile
import com.example.pinjamsejuta.model.customer.CustomerProfileResponse
import com.example.pinjamsejuta.data.repository.CustomerRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountViewModel(private val repository: CustomerRepository) : ViewModel() {

    private val _userProfile = MutableLiveData<CustomerProfile>()
    val userProfile: LiveData<CustomerProfile> get() = _userProfile

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _text = MutableLiveData<String>("This is account Fragment")
    val text: LiveData<String> get() = _text

    // Fungsi untuk memuat profil user
    fun loadUserProfile() {
        repository.getUserProfile().enqueue(object : Callback<CustomerProfileResponse> {
            override fun onResponse(
                call: Call<CustomerProfileResponse>,
                response: Response<CustomerProfileResponse>
            ) {
                if (response.isSuccessful) {
                    // Cek apakah data profil user ada
                    response.body()?.data?.let {
                        _userProfile.postValue(it)
                    } ?: run {
                        _error.postValue("Gagal memuat profil user: Data tidak ditemukan.")
                    }
                } else {
                    // Tangani jika response tidak sukses
                    _error.postValue("Gagal memuat profil user: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<CustomerProfileResponse>, t: Throwable) {
                // Tangani error koneksi atau masalah lain
                _error.postValue("Terjadi kesalahan: ${t.message}")
            }
        })
    }
}
