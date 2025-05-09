package com.example.pinjamsejuta.model.loan

data class PlafondResponse(
    val status: Int,
    val message: String,
    val data: List<Plafond>
)