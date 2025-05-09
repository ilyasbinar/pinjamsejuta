package com.example.pinjamsejuta.model.auth

// RegisterResponse.kt
data class RegisterResponse(
    val timestamp: String,
    val status: Int,
    val message: String,
    val data: RegisterData
)

data class RegisterData(
    val id: String,
    val email: String,
    val name: String,
    val role: String,
    val userType: String,
    val roleFeatures: List<String>,
    val plafond: String,
    val remainingPlafond: Double
)
