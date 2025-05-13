package com.example.pinjamsejuta.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.pinjamsejuta.R
import com.example.pinjamsejuta.adapter.PlafondAdapter
import com.example.pinjamsejuta.databinding.FragmentHomeBinding
import com.example.pinjamsejuta.model.loan.PlafondResponse
import com.example.pinjamsejuta.data.remote.api.LoanApiService
import com.example.pinjamsejuta.data.remote.network.SakuBCAClient
import com.example.pinjamsejuta.ui.loan.LoanSimulationActivity
import com.example.pinjamsejuta.utils.ProgressUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PlafondAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerPlafond)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Menampilkan loading sebelum data dimuat
        val lottieLoading = view.findViewById<LottieAnimationView>(R.id.lottieLoading)
        ProgressUtil.showLoading(lottieLoading, recyclerView)

        val apiService = SakuBCAClient.getInstance(requireContext()).create(LoanApiService::class.java)
        apiService.getPlafonds().enqueue(object : Callback<PlafondResponse> {
            override fun onResponse(call: Call<PlafondResponse>, response: Response<PlafondResponse>) {
                // Menyembunyikan ProgressBar setelah data diterima
                ProgressUtil.hideLoading(lottieLoading, recyclerView)

                if (response.isSuccessful && response.body() != null) {
                    val plafonds = response.body()!!.data
                    adapter = PlafondAdapter(plafonds) { plafon ->
                        Toast.makeText(context, "Ajukan untuk ${plafon.name}", Toast.LENGTH_SHORT).show()
                        val intent = Intent(requireContext(), LoanSimulationActivity::class.java)
                        startActivity(intent)
                    }
                    recyclerView.adapter = adapter
                } else {
                    Toast.makeText(context, "Data tidak valid", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PlafondResponse>, t: Throwable) {
                // Menyembunyikan ProgressBar dan memberi pesan error
                ProgressUtil.hideLoading(lottieLoading, recyclerView)
                Toast.makeText(context, "Gagal memuat data", Toast.LENGTH_SHORT).show()
            }
        })
    }
}