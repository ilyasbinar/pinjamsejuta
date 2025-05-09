package com.example.pinjamsejuta.model.loan

data class Plafond(
    val id: String,
    val name: String,
    val maxAmount: Double,
    val interestRate: Double,
    val minTenor: Int,
    val maxTenor: Int
)