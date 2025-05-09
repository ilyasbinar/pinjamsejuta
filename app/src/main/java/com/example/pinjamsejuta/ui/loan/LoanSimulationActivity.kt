package com.example.pinjamsejuta.ui.loan

import android.icu.text.NumberFormat
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.pinjamsejuta.R
import com.example.pinjamsejuta.databinding.ActivityLoanSimulationBinding
import java.util.Locale

class LoanSimulationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoanSimulationBinding

    private val minAmount = 1_000_000
    private val maxAmount = 10_000_000
    private val maxTenor = 24 // bulan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoanSimulationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Menghubungkan Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Menampilkan tombol back di kiri atas
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setHomeAsUpIndicator(R.drawable.bac) // Ganti dengan ikon back yang diinginkan

        // Menangani klik pada tombol back
//        toolbar.setNavigationOnClickListener {
//            onBackPressed() // Menangani aksi kembali
//        }

        setupLoanSeekBar()
        setupTenorSeekBar()
        setupCalculateButton()
    }

    private fun setupLoanSeekBar() {
        binding.seekBarLoan.max = 100
        binding.seekBarLoan.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val amount = minAmount + (progress * (maxAmount - minAmount) / 100)
                binding.tvLoanAmount.text = "Jumlah Pinjaman: Rp${amount.toLocaleString()}"
                binding.seekBarLoan.tag = amount // Simpan untuk nanti
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        // Set nilai awal
        binding.seekBarLoan.progress = 50
    }

    private fun setupTenorSeekBar() {
        binding.seekBarTenor.max = maxTenor - 1 // 0-based index
        binding.seekBarTenor.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val tenor = progress + 1 // karena index 0 = 1 bulan
                binding.tvTenor.text = "Tenor: $tenor bulan"
                binding.seekBarTenor.tag = tenor
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        // Set nilai awal
        binding.seekBarTenor.progress = 5 // default 6 bulan
    }

    private fun setupCalculateButton() {
        binding.btnCalculate.setOnClickListener {
            val amount = binding.seekBarLoan.tag as? Int ?: return@setOnClickListener
            val tenor = binding.seekBarTenor.tag as? Int ?: return@setOnClickListener

            val interestRate = 0.02 // 2% per bulan
            val monthlyInstallment = (amount * (1 + interestRate * tenor)) / tenor
            val totalPayment = monthlyInstallment * tenor

            binding.tvResult.text = """
                Total Pembayaran: Rp${totalPayment.toInt().toLocaleString()}
                Cicilan Per Bulan: Rp${monthlyInstallment.toInt().toLocaleString()}
            """.trimIndent()

            binding.btnAjukan.visibility = View.VISIBLE
        }
    }

    private fun Int.toLocaleString(): String {
        return NumberFormat.getNumberInstance(Locale("in", "ID")).format(this)
    }
}