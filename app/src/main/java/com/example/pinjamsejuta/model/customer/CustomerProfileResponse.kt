package com.example.pinjamsejuta.model.customer

import com.example.pinjamsejuta.model.loan.Plafond

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
