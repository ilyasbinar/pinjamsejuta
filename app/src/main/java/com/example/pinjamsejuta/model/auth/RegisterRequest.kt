package com.example.pinjamsejuta.model.auth

data class RegisterRequest(
    val name: String,
    val email: String,
    val jenis_kelamin: String,
    val password: String
)
