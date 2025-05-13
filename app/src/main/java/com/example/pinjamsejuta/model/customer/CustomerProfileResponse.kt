package com.example.pinjamsejuta.model.customer

data class CustomerProfileResponse(
    val status: Int,
    val message: String,
    val data: CustomerProfile
)

data class CustomerProfile(
    val id: String,
    val name: String,
    val email: String,
    val role: String,
    val jenisKelamin: String,
    val customerDetails: CustomerDetails?
)

data class CustomerDetails(
    val id: String,
    val ttl: String?,
    val alamat: String?,
    val noTelp: String?,
    val nik: String?,
    val namaIbuKandung: String?,
    val pekerjaan: String?,
    val gaji: String?,
    val noRek: String?,
    val statusRumah: String?,
    val plafond: Plafond?
)

data class Plafond(
    val id: String,
    val name: String,
    val maxAmount: Double,
    val interestRate: Double,
    val minTenor: Int,
    val maxTenor: Int
)
