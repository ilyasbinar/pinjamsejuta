package com.example.pinjamsejuta.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pinjamsejuta.model.loan.Plafond
import com.example.pinjamsejuta.model.loan.PlafondResponse
import com.example.pinjamsejuta.data.repository.LoanMasterRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val loanMasterRepository: LoanMasterRepository) : ViewModel() {

    private val _plafonds = MutableLiveData<List<Plafond>>()
    val plafonds: LiveData<List<Plafond>> = _plafonds

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun fetchPlafonds() {
        _loading.value = true
        loanMasterRepository.getPlafonds().enqueue(object : Callback<PlafondResponse> {
            override fun onResponse(call: Call<PlafondResponse>, response: Response<PlafondResponse>) {
                _loading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _plafonds.value = response.body()!!.data
                } else {
                    _error.value = "Data tidak valid"
                }
            }

            override fun onFailure(call: Call<PlafondResponse>, t: Throwable) {
                _loading.value = false
                _error.value = "Gagal memuat data: ${t.message}"
            }
        })
    }
}
