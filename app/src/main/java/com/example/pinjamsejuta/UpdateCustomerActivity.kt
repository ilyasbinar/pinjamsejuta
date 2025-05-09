package com.example.pinjamsejuta

import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.pinjamsejuta.model.file.FileUploadResponse
import com.example.pinjamsejuta.network.FileUploadService
import com.example.pinjamsejuta.network.SakuBCAIlyasClient
import com.example.pinjamsejuta.repository.UploadRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class UpdateCustomerActivity : AppCompatActivity() {

    private var selectedFileUri: Uri? = null

    private lateinit var btnSelectFile: Button
    private lateinit var btnUpload: Button
    private lateinit var txtFileName: TextView

    private val fileUploadService by lazy {
        SakuBCAIlyasClient.instance.create(FileUploadService::class.java)
    }

    private val repository by lazy {
        UploadRepository(fileUploadService)
    }

    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            selectedFileUri = uri
            txtFileName.text = getFileName(uri)
            btnUpload.isEnabled = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_customer)

        btnSelectFile = findViewById(R.id.btnSelectFile)
        btnUpload = findViewById(R.id.btnUpload)
        txtFileName = findViewById(R.id.txtFileName)

        btnSelectFile.setOnClickListener {
            filePickerLauncher.launch("*/*")
        }

        btnUpload.setOnClickListener {
            selectedFileUri?.let { uri ->
                uploadFile(uri)
            } ?: Toast.makeText(this, "Please select a file", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    result = it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                }
            }
        }
        return result ?: uri.path?.substringAfterLast('/') ?: "unknown"
    }

    private fun uploadFile(uri: Uri) {
        val inputStream = contentResolver.openInputStream(uri)
        if (inputStream == null) {
            Toast.makeText(this@UpdateCustomerActivity, "File not accessible", Toast.LENGTH_SHORT).show()
            return
        }

        val fileName = getFileName(uri)
        val requestBody = inputStream.readBytes().toRequestBody("*/*".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("file", fileName, requestBody)

        repository.uploadFile(filePart, object : retrofit2.Callback<FileUploadResponse> {
            override fun onResponse(
                call: retrofit2.Call<FileUploadResponse>,
                response: retrofit2.Response<FileUploadResponse>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    Toast.makeText(this@UpdateCustomerActivity, body?.message ?: "Success", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@UpdateCustomerActivity, "Upload failed: ${response.code()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<FileUploadResponse>, t: Throwable) {
                Log.e("Upload", "Error: ${t.message}")
                Toast.makeText(this@UpdateCustomerActivity, "Upload error", Toast.LENGTH_LONG).show()
            }
        })

//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val response = repository.uploadFile(filePart)
//                runOnUiThread {
//                    if (response.isSuccessful) {
//                        val body: FileUploadResponse? = response.body()
//                        Toast.makeText(this@UpdateCustomerActivity, body?.message ?: "Success", Toast.LENGTH_LONG).show()
//                    } else {
//                        Toast.makeText(this@UpdateCustomerActivity, "Upload failed: ${response.code()}", Toast.LENGTH_LONG).show()
//                    }
//                }
//            } catch (e: Exception) {
//                Log.e("Upload", "Error: ${e.message}")
//                runOnUiThread {
//                    Toast.makeText(this@UpdateCustomerActivity, "Upload error", Toast.LENGTH_LONG).show()
//                }
//            }
//        }
    }
}

