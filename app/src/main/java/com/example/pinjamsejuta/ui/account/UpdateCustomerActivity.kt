package com.example.pinjamsejuta.ui.account

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.pinjamsejuta.R
import com.example.pinjamsejuta.model.file.FileUploadResponse
import com.example.pinjamsejuta.data.remote.api.FileUploadService
import com.example.pinjamsejuta.data.remote.network.SakuBCAIlyasClient
import com.example.pinjamsejuta.data.repository.UploadRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class UpdateCustomerActivity : AppCompatActivity() {

    private var selectedFileUri: Uri? = null
    private var cameraImageUri: Uri? = null

    private lateinit var btnSelectFile: Button
    private lateinit var btnUpload: Button
    private lateinit var btnCaptureCamera: Button
    private lateinit var txtFileName: TextView
    private lateinit var imgPreview: ImageView
    private lateinit var imgPreview2: ImageView

    private lateinit var cameraLauncher: androidx.activity.result.ActivityResultLauncher<Uri>

    private val fileUploadService by lazy {
        SakuBCAIlyasClient.instance.create(FileUploadService::class.java)
    }

    private val repository by lazy {
        UploadRepository(fileUploadService)
    }

    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> handleFileSelected(uri) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_customer)

        initViews()
        setupFilePicker()
        setupCameraCapture()
        setupUploadButton()
    }

    private fun initViews() {
        btnSelectFile = findViewById(R.id.btnSelectFile)
        btnUpload = findViewById(R.id.btnUpload)
        btnCaptureCamera = findViewById(R.id.btnCaptureCamera)
        txtFileName = findViewById(R.id.txtFileName)
        imgPreview = findViewById(R.id.imgPreview)
        imgPreview2 = findViewById(R.id.imgPreview2)
    }

    private fun setupFilePicker() {
        btnSelectFile.setOnClickListener {
            filePickerLauncher.launch("image/*")
        }
    }

    private fun handleFileSelected(uri: Uri?) {
        if (uri == null) return

        val mimeType = contentResolver.getType(uri)
        if (mimeType?.startsWith("image/") == true) {
            selectedFileUri = uri
            txtFileName.text = getFileName(uri)
            imgPreview.setImageURI(uri)
            imgPreview.visibility = ImageView.VISIBLE
            btnUpload.isEnabled = true
        } else {
            selectedFileUri = null
            imgPreview.setImageDrawable(null)
            imgPreview.visibility = ImageView.GONE
            txtFileName.text = "Please select a valid image file"
            btnUpload.isEnabled = false
            showToast("Only image files are allowed")
        }
    }

    private fun setupCameraCapture() {
        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success && cameraImageUri != null) {
                imgPreview2.setImageURI(cameraImageUri)
                imgPreview2.visibility = ImageView.VISIBLE
                txtFileName.text = cameraImageUri.toString()
                showToast("Image captured")
            } else {
                showToast("Capture cancelled")
            }
        }

        btnCaptureCamera.setOnClickListener {
            if (checkCameraPermission()) {
                launchCamera()
            }
        }
    }

    private fun checkCameraPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 100)
            false
        } else {
            true
        }
    }

    private fun launchCamera() {
        val imageFile = createImageFile()
        cameraImageUri = FileProvider.getUriForFile(
            this,
            "${packageName}.fileprovider",
            imageFile
        )
        cameraLauncher.launch(cameraImageUri)
    }

    private fun setupUploadButton() {
        btnUpload.setOnClickListener {
            if (selectedFileUri != null && cameraImageUri != null) {
                uploadFiles(selectedFileUri!!, cameraImageUri!!)
            } else {
                showToast("Please select both files")
            }
        }
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US).format(Date())
        val storageDir = getExternalFilesDir("Pictures")
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }

    private fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            contentResolver.query(uri, null, null, null, null)?.use {
                if (it.moveToFirst()) {
                    result = it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                }
            }
        }
        return result ?: uri.path?.substringAfterLast('/') ?: "unknown"
    }

    private fun uploadFiles(file1Uri: Uri, file2Uri: Uri) {
        val filePart1 = prepareFilePart("file", file1Uri) ?: return
        val filePart2 = prepareFilePart("file2", file2Uri) ?: return

        repository.uploadFile2(filePart1, filePart2, object : retrofit2.Callback<FileUploadResponse> {
            override fun onResponse(
                call: retrofit2.Call<FileUploadResponse>,
                response: retrofit2.Response<FileUploadResponse>
            ) {
                val message = if (response.isSuccessful) {
                    response.body()?.message ?: "Success"
                } else {
                    "Upload failed: ${response.code()}"
                }
                showToast(message)
            }

            override fun onFailure(call: retrofit2.Call<FileUploadResponse>, t: Throwable) {
                Log.e("Upload", "Error: ${t.message}")
                showToast("Upload error")
            }
        })
    }

    private fun prepareFilePart(partName: String, uri: Uri): MultipartBody.Part? {
        val inputStream = contentResolver.openInputStream(uri)
        if (inputStream == null) {
            showToast("File not accessible")
            return null
        }

        val fileName = getFileName(uri)
        val requestBody = inputStream.readBytes().toRequestBody("*/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, fileName, requestBody)
    }

    private fun showToast(message: String) {
        Toast.makeText(this@UpdateCustomerActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            val message = if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                "Izin kamera diberikan"
            } else {
                "Izin kamera ditolak"
            }
            showToast(message)
        }
    }
}