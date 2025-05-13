package com.example.pinjamsejuta.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.example.pinjamsejuta.model.auth.RegisterRequest
import com.example.pinjamsejuta.model.auth.RegisterResponse
import com.example.pinjamsejuta.data.repository.AuthRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _registerResult = MutableLiveData<Result<RegisterResponse>>()
    val registerResult: LiveData<Result<RegisterResponse>> get() = _registerResult

    fun registerCustomer(request: RegisterRequest) {
        repository.registerUser(request).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    _registerResult.postValue(Result.success(response.body()!!))
                } else {
                    _registerResult.postValue(Result.failure(Throwable("Error ${response.code()}")))
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _registerResult.postValue(Result.failure(t))
            }
        })
    }
}
