package com.example.pinjamsejuta.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.pinjamsejuta.databinding.ActivityRegisterBinding
import com.example.pinjamsejuta.model.auth.RegisterRequest
import com.example.pinjamsejuta.data.remote.api.AuthApiService
import com.example.pinjamsejuta.data.remote.network.SakuBCAClient
import com.example.pinjamsejuta.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel using a Factory
        val apiService = SakuBCAClient.getInstance(this).create(AuthApiService::class.java)
        val factory = RegisterViewModelFactory(apiService)
        registerViewModel = ViewModelProvider(this, factory)[RegisterViewModel::class.java]

        // Using ViewBinding to access views directly
        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val selectedId = binding.radioGender.checkedRadioButtonId
            val gender = findViewById<RadioButton>(selectedId)?.text.toString().uppercase()

            val request = RegisterRequest(name, email, gender, password)
            registerViewModel.registerCustomer(request)
        }

        // Observe register result
        registerViewModel.registerResult.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, "Success: ${it.message}", Toast.LENGTH_SHORT).show()

                // Redirect to LoginActivity
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish() // Optional: prevents user from returning to Register screen
            }
            result.onFailure {
                Toast.makeText(this, "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}