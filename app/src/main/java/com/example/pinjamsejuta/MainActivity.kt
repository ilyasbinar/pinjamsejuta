package com.example.pinjamsejuta

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.pinjamsejuta.databinding.ActivityMainBinding
import com.example.pinjamsejuta.ui.login.LoginActivity
import com.example.pinjamsejuta.utils.SharedPrefsUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import android.Manifest
import android.content.pm.PackageManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isLogin = false

    private fun getFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d("FCM", "FCM Token: $token")
                // Here you can save the token, send it to your server, etc.
            } else {
                Log.w("FCM", "Fetching FCM token failed", task.exception)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Android 13+: check permission before showing
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }


        FirebaseApp.initializeApp(this) // Optional, biasanya otomatis
//        getFCMToken()

        isLogin = SharedPrefsUtils.isLoggedIn(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set toolbar as the ActionBar
        setSupportActionBar(binding.topAppBar)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard,
                R.id.navigation_notifications, R.id.navigation_account
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        if (savedInstanceState == null) {
            navController.navigate(R.id.navigation_home)
        }

        // Gantikan setupWithNavController agar kita bisa handle sendiri
        navView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_account -> {
                    if (isLogin) {
                        navController.navigate(R.id.navigation_account)
                    } else {
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    }
                    true
                }
                else -> {
                    navController.navigate(menuItem.itemId)
                    true
                }
            }
        }
    }
}
