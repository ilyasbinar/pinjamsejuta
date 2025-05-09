package com.example.pinjamsejuta.model.auth

data class LoginResponse(
    val timestamp: String,
    val status: Int,
    val message: String,
    val data: LoginDataWrapper
)

data class LoginDataWrapper(
    val data: LoginData,
    val status: String
)

data class LoginData(
    val firstLogin: Boolean,
    val jwt: Jwt
)

data class Jwt(
    val token: String,
    val type: String,
    val username: String,
    val features: List<String>,
    val role: String,
    val name: String
)

sealed class LoginResult {
    data class Success(
        val message: String,
        val jwt: Jwt
    ) : LoginResult()

    data class Error(val message: String) : LoginResult()
}


