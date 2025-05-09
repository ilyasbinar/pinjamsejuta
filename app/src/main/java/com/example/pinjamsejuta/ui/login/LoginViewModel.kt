package com.example.pinjamsejuta.ui.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pinjamsejuta.model.auth.LoginRequest
import com.example.pinjamsejuta.model.auth.LoginResponse
import com.example.pinjamsejuta.model.auth.LoginResult
import com.example.pinjamsejuta.repository.AuthRepository
import com.example.pinjamsejuta.utils.SharedPrefsUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.lifecycle.viewModelScope
import com.example.pinjamsejuta.network.AuthApiService
import kotlinx.coroutines.launch
import java.io.IOException


class LoginViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _loginStatus = MutableLiveData<LoginResult>()
    val loginStatus: LiveData<LoginResult> = _loginStatus

    // Fungsi login untuk validasi dan pengaturan status
    fun loginCustomer(email: String, password: String, context: Context) {
        val loginRequest = LoginRequest(email, password)
        val call = repository.loginCustomer(loginRequest)

        // Mengirimkan request login
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()
                    val jwt = loginResponse?.data?.data?.jwt
                    if (jwt != null) {
                        SharedPrefsUtils.setLoginStatus(context, true)
                        SharedPrefsUtils.setToken(context, jwt.token)
                    }

                    if (jwt != null) {
                        _loginStatus.value = LoginResult.Success(
                            message = loginResponse.message,
                            jwt = jwt,
//                            username = data.username,
//                            features = data.features,
//                            role = data.role,
//                            name = data.name
                        )
                    } else {
                        _loginStatus.postValue(LoginResult.Error("Login failed: JWT is null"))
                    }


                } else {
                    _loginStatus.postValue(LoginResult.Error("Login failed: ${response.message()}"))
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _loginStatus.postValue(LoginResult.Error("Network error: ${t.message}"))
                Log.e("Login", "Error: ${t.message}")
            }
        })
    }

    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            try {
                val response = repository.loginWithGoogle(idToken)
                if (response.isSuccessful) {
                    Log.d("Login", "Response body: ${response.body()}")
                    val body = response.body()
                    if (body != null) {
                        val message = body.message
                        val jwt = body.data.data.jwt
                        _loginStatus.postValue(LoginResult.Success(message , jwt ))
                    } else {
                        _loginStatus.postValue(LoginResult.Error("Response Google kosong"))
                    }
                } else {
                    _loginStatus.postValue(LoginResult.Error("Login Google gagal: ${response.code()}"))
                }
            } catch (e: IOException) {
                _loginStatus.postValue(LoginResult.Error("Tidak bisa konek ke server"))
            } catch (e: Exception) {
                _loginStatus.postValue(LoginResult.Error("Kesalahan: ${e.localizedMessage}"))
            }
        }
    }
}