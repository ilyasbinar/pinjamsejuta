package com.example.pinjamsejuta.model.customer

data class ApiResponse<T>(
    val status: Boolean,
    val message: String,
    val data: T?
)
