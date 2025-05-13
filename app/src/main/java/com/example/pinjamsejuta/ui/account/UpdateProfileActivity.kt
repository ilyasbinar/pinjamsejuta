package com.example.pinjamsejuta.ui.account

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pinjamsejuta.R
import com.example.pinjamsejuta.data.remote.api.FileUploadService
import com.example.pinjamsejuta.data.remote.network.SakuBCAIlyasClient
import com.example.pinjamsejuta.data.repository.UploadRepository
import com.example.pinjamsejuta.databinding.ActivityUpdateProfileBinding
import com.example.pinjamsejuta.model.file.FileUploadResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UpdateProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateProfileBinding

    private var selectedFileUri: Uri? = null
    private var cameraImageUri: Uri? = null

    private val fileUploadService by lazy {
        SakuBCAIlyasClient.instance.create(FileUploadService::class.java)
    }

    private val repository by lazy {
        UploadRepository(fileUploadService)
    }

    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        handleFileSelection(uri)
    }

    private lateinit var cameraLauncher: androidx.activity.result.ActivityResultLauncher<Uri>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        setupCameraLauncher()
    }

    private fun setupListeners() {
        binding.btnSelectFile.setOnClickListener {
            filePickerLauncher.launch("image/*")
        }

        binding.btnCaptureCamera.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 100)
            } else {
                launchCamera()
            }
        }

        binding.btnUpload.setOnClickListener {
            if (selectedFileUri != null && cameraImageUri != null) {
                uploadFile(selectedFileUri!!, cameraImageUri!!)
            } else {
                Toast.makeText(this, "Please select both files", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupCameraLauncher() {
        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success && cameraImageUri != null) {
                binding.imgPreview2.setImageURI(cameraImageUri)
                binding.imgPreview2.visibility = android.view.View.VISIBLE
                binding.txtFileName.text = cameraImageUri.toString()
                Toast.makeText(this, "Image captured", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Capture cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun launchCamera() {
        val imageFile = createImageFile()
        cameraImageUri = FileProvider.getUriForFile(
            this,
            "${packageName}.fileprovider",
            imageFile
        )
        cameraImageUri?.let {
            cameraLauncher.launch(it)
        } ?: run {
            Toast.makeText(this, "Failed to create image file", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleFileSelection(uri: Uri?) {
        if (uri != null) {
            val mimeType = contentResolver.getType(uri)
            if (mimeType?.startsWith("image/") == true) {
                selectedFileUri = uri
                binding.txtFileName.text = getFileName(uri)
                binding.imgPreview.setImageURI(uri)
                binding.imgPreview.visibility = android.view.View.VISIBLE
                binding.btnUpload.isEnabled = true
            } else {
                selectedFileUri = null
                binding.imgPreview.setImageDrawable(null)
                binding.imgPreview.visibility = android.view.View.GONE
                binding.txtFileName.text = "Please select a valid image file"
                binding.btnUpload.isEnabled = false
                Toast.makeText(this, "Only image files are allowed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadFile(uri: Uri, uri2: Uri) {
        val part1 = createMultipart(uri, "file") ?: return
        val part2 = createMultipart(uri2, "file2") ?: return

        repository.uploadFile2(part1, part2, object : Callback<FileUploadResponse> {
            override fun onResponse(call: Call<FileUploadResponse>, response: Response<FileUploadResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@UpdateProfileActivity, response.body()?.message ?: "Success", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@UpdateProfileActivity, "Upload failed: ${response.code()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                Log.e("Upload", "Error: ${t.message}")
                Toast.makeText(this@UpdateProfileActivity, "Upload error", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun createMultipart(uri: Uri, name: String): MultipartBody.Part? {
        val inputStream = contentResolver.openInputStream(uri)
        if (inputStream == null) {
            Toast.makeText(this, "File not accessible", Toast.LENGTH_SHORT).show()
            return null
        }
        val fileName = getFileName(uri)
        val requestBody = inputStream.readBytes().toRequestBody("*/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(name, fileName, requestBody)
    }

    private fun createImageFile(): File {
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
        val timeStamp = sdf.format(Date())
        val storageDir = getExternalFilesDir("Pictures")
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }

    private fun getFileName(uri: Uri): String {
        if (uri.scheme == "content") {
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    return it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                }
            }
        }
        return uri.path?.substringAfterLast('/') ?: "unknown"
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Izin kamera diberikan", Toast.LENGTH_SHORT).show()
                launchCamera()
            } else {
                Toast.makeText(this, "Izin kamera ditolak", Toast.LENGTH_SHORT).show()
            }
        }
    }
}