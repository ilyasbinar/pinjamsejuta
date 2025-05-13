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
    val userProfile: LiveData<CustomerProfile> = _userProfile

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _text = MutableLiveData<String>().apply {
        value = "This is account Fragment"
    }
    val text: LiveData<String> = _text

    fun loadUserProfile() {
        repository.getUserProfile().enqueue(object : Callback<CustomerProfileResponse> {
            override fun onResponse(
                call: Call<CustomerProfileResponse>,
                response: Response<CustomerProfileResponse>
            ) {
                if (response.isSuccessful && response.body()?.data != null) {
                    _userProfile.postValue(response.body()!!.data)
                } else {
                    _error.postValue("Gagal memuat profil user")
                }
            }

            override fun onFailure(call: Call<CustomerProfileResponse>, t: Throwable) {
                _error.postValue("Terjadi kesalahan: ${t.message}")
            }
        })
    }
}