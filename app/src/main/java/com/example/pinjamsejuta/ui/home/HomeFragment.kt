package com.example.pinjamsejuta.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pinjamsejuta.adapter.PlafondAdapter
import com.example.pinjamsejuta.databinding.FragmentHomeBinding
import com.example.pinjamsejuta.ui.loan.LoanSimulationActivity
import com.example.pinjamsejuta.utils.ProgressUtil

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PlafondAdapter
    private val viewModel: HomeViewModel by viewModels { HomeViewModelFactory(requireContext()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()

        viewModel.fetchPlafonds()
    }

    private fun setupRecyclerView() {
        adapter = PlafondAdapter(emptyList()) { plafon ->
            Toast.makeText(context, "Ajukan untuk ${plafon.name}", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), LoanSimulationActivity::class.java))
        }
        binding.recyclerPlafond.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerPlafond.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                ProgressUtil.showLoading(binding.lottieLoading, binding.recyclerPlafond)
            } else {
                ProgressUtil.hideLoading(binding.lottieLoading, binding.recyclerPlafond)
            }
        }

        viewModel.plafonds.observe(viewLifecycleOwner) { list ->
            adapter.updateData(list)
        }

        viewModel.error.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
