package com.example.pinjamsejuta.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.pinjamsejuta.MainActivity
import com.example.pinjamsejuta.databinding.ActivityLoginBinding
import com.example.pinjamsejuta.model.auth.LoginResult
import com.example.pinjamsejuta.data.remote.api.AuthApiService
import com.example.pinjamsejuta.data.remote.network.SakuBCAClient
import com.example.pinjamsejuta.ui.register.RegisterActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class LoginActivity : ComponentActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 1001


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //GOOGLE SIGN IN
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("662217307758-8vsv0e5npgm1g0c699bi80cfl05q83p8.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.buttonGoogleSignIn.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
        //END OF GOOGLE SIGN IN

        // Init ViewModel dengan Retrofit API
        val apiService = SakuBCAClient.getInstance(this).create(AuthApiService::class.java)
        val factory = LoginViewModelFactory(apiService)
        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        // Observe hasil login
        loginViewModel.loginStatus.observe(this) { result ->
            when (result) {
                is LoginResult.Success -> {
                    Toast.makeText(this, "Login berhasil!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("IS_LOGIN", true)
                    startActivity(intent)
                    finish()
                }

                is LoginResult.Error -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Event klik tombol login
        binding.buttonLogin.setOnClickListener {
            performValidation();
        }

        //BUTTON REGISTER
        binding.buttonRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        //END BUTTON REGISTER
    }

    private fun performValidation() {
        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()
        var isValid = true

        // Validasi email
        if (email.isBlank()) {
            binding.emailInputLayout.error = "Email tidak boleh kosong"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailInputLayout.error = "Format email tidak valid"
            isValid = false
        } else {
            binding.emailInputLayout.error = null
        }

        // Validasi password
        if (password.isBlank()) {
            binding.passwordInputLayout.error = "Password tidak boleh kosong"
            isValid = false
        } else {
            binding.passwordInputLayout.error = null
        }

        // Jika valid, lanjutkan login
        if (isValid) {
            loginViewModel.loginCustomer(email, password, this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account?.idToken
                if (idToken != null) {
                    // kirim ke backend
                    loginViewModel.loginWithGoogle(idToken)
                } else {
                    Toast.makeText(this, "ID Token Google tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign-in gagal: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
