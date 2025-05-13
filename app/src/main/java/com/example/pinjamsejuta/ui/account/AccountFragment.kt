package com.example.pinjamsejuta.ui.account

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pinjamsejuta.MainActivity
import com.example.pinjamsejuta.R
import com.example.pinjamsejuta.databinding.FragmentAccountBinding
import com.example.pinjamsejuta.utils.SharedPrefsUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener

class AccountFragment : Fragment() {

    private lateinit var viewModel: AccountViewModel

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    // Added a variable for the coordinates TextView
    private lateinit var coordinatesTextView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAccountBinding.bind(view)

        //PROFILE
        viewModel = ViewModelProvider(this, AccountViewModelFactory(requireContext()))[AccountViewModel::class.java]
        viewModel.loadUserProfile()
        viewModel.userProfile.observe(viewLifecycleOwner) { profile ->
            binding.tvUserName.text = profile.name
            binding.tvEmail.text = profile.email
        }
        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }
        //END PROFILE

        //LOGOUT
        binding.buttonLogout.setOnClickListener {
            performLogout()
        }
        //END LOGOUT

        //EDIT2
        binding.button2Edit.setOnClickListener {

            // Pindah ke halaman login atau main activity
            val intent = Intent(requireContext(), UpdateCustomerActivity::class.java)
            startActivity(intent)
        }
        //END of edit2

    }

    private fun performLogout() {
        SharedPrefsUtils.clearLoginData(requireContext())
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    //tidak dipakai.
    fun onCreateView2(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)

        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val textView: TextView = binding.textAccount
        accountViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        // Initialize coordinates TextView
        coordinatesTextView = binding.root.findViewById(R.id.text_coordinates)

        // Temukan tombol logout
        val logoutButton: Button = binding.root.findViewById(R.id.buttonLogout)
        // Menangani klik tombol logout
        logoutButton.setOnClickListener {

        }


        // Temukan tombol edit
        val editButton: Button = binding.root.findViewById(R.id.buttonEdit)
        // Menangani klik tombol logout
        editButton.setOnClickListener {

            // Pindah ke halaman login atau main activity
            val intent = Intent(requireContext(), UpdateCustomerActivity::class.java)
            startActivity(intent)
        }

        // Temukan tombol edit
        val edit2Button: Button = binding.root.findViewById(R.id.button2Edit)
        // Menangani klik tombol logout
        edit2Button.setOnClickListener {

            // Pindah ke halaman login atau main activity
            val intent = Intent(requireContext(), UpdateCustomerActivity::class.java)
            startActivity(intent)
        }

        // Inisialisasi FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Register the permission launcher
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Izin diberikan, mendapatkan lokasi
                getCurrentLocation()
            } else {
                // Izin ditolak, beri penanganan yang sesuai
            }
        }

        // Memeriksa izin lokasi dan mendapatkan lokasi jika diizinkan
        checkAndRequestLocationPermission()

        return root
    }

    private fun checkAndRequestLocationPermission() {
        // Memeriksa apakah izin sudah diberikan
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Izin sudah diberikan, mendapatkan lokasi
            getCurrentLocation()
        } else {
            // Jika izin belum diberikan, meminta izin
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun getCurrentLocation() {
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener(requireActivity(), object : OnSuccessListener<Location> {
                    override fun onSuccess(location: Location?) {
                        if (location != null) {
                            // Lokasi ditemukan, lakukan sesuatu dengan data lokasi
                            val latitude = location.latitude
                            val longitude = location.longitude
                            // Tampilkan atau simpan lokasi sesuai kebutuhan
                            coordinatesTextView.text = "Latitude: $latitude, Longitude: $longitude"
                        } else {
                            // Lokasi tidak ditemukan
                            coordinatesTextView.text = "Lokasi tidak ditemukan"
                        }
                    }
                })
        } catch (e: SecurityException) {
            // Tangani SecurityException jika izin belum diberikan
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
