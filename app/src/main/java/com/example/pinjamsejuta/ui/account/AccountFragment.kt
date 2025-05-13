package com.example.pinjamsejuta.ui.account

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    // View components
    private lateinit var coordinatesTextView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAccountBinding.bind(view)

        initializeViewModel()
        setupUI()
        setupLocationService()
    }

    private fun initializeViewModel() {
        // Initialize and observe the ViewModel for user profile data
        viewModel = ViewModelProvider(this, AccountViewModelFactory(requireContext()))[AccountViewModel::class.java]
        viewModel.loadUserProfile()

        // Observe user profile and error messages
        viewModel.userProfile.observe(viewLifecycleOwner) { profile ->
            binding.tvUserName.text = profile.name
            binding.tvEmail.text = profile.email
        }
        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupUI() {
        // Setup logout and edit buttons
        binding.buttonLogout.setOnClickListener { performLogout() }
        binding.button2Edit.setOnClickListener { navigateToUpdateCustomer() }
    }

    private fun performLogout() {
        // Clear login data and navigate to the main activity
        SharedPrefsUtils.clearLoginData(requireContext())
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun navigateToUpdateCustomer() {
        // Navigate to UpdateCustomerActivity
        val intent = Intent(requireContext(), UpdateCustomerActivity::class.java)
        startActivity(intent)
    }

    private fun setupLocationService() {
        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Register the permission launcher
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                getCurrentLocation()
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

        // Check and request location permission
        checkAndRequestLocationPermission()
    }

    private fun checkAndRequestLocationPermission() {
        // Check if the location permissions are granted
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation()
        } else {
            // Request location permission if not granted
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun getCurrentLocation() {
        // Get the current location using FusedLocationProviderClient
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity(), object : OnSuccessListener<Location> {
                override fun onSuccess(location: Location?) {
                    if (location != null) {
                        val latitude = location.latitude
                        val longitude = location.longitude
                        coordinatesTextView.text = "Latitude: $latitude, Longitude: $longitude"
                    } else {
                        coordinatesTextView.text = "Location not found"
                    }
                }
            })
        } catch (e: SecurityException) {
            // Handle exception if permissions are not granted
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
