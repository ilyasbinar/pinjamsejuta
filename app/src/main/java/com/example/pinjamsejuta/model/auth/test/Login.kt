package com.example.pinjamsejuta.model.auth.test

data class Login(
    val data: Data,
    val message: String,
    val status: Int,
    val timestamp: String
)

data class Data(
    val data: DataX,
    val status: String
)

data class DataX(
    val firstLogin: Boolean,
    val jwt: Jwt
)

data class Jwt(
    val features: List<String>,
    val name: String,
    val role: String,
    val token: String,
    val type: String,
    val username: String
)