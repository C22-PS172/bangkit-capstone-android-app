package com.bangkit.bangkitcapstoneproject.activity.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bangkit.bangkitcapstoneproject.R
import com.bangkit.bangkitcapstoneproject.activity.ui.result.ResultActivity
import com.bangkit.bangkitcapstoneproject.activity.utils.*
import com.bangkit.bangkitcapstoneproject.databinding.ActivityHomeBinding
import java.io.File

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var photoPath: String
    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                message(getString(R.string.permission))
                finish()
            }
        }
    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()

        homeViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        homeViewModel.message.observe(this) { message ->
            message(message)
        }

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.camera.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.resolveActivity(packageManager)
            createTempFile(application).also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    this@HomeActivity,
                    "com.bangkit.bangkitcapstoneproject",
                    it
                )
                photoPath = it.absolutePath
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                homeViewModel.imgUri = photoURI.toString()
                homeViewModel.path = it.absolutePath
                launcherIntentCamera.launch(intent)
            }
        }
        binding.gallery.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            val chooser = Intent.createChooser(intent, getString(R.string.choosePicture))
            launcherIntentGallery.launch(chooser)
        }
    }

    private fun message(string: String?) {
        Toast.makeText(this@HomeActivity, string, Toast.LENGTH_SHORT).show()
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            homeViewModel.file = File(homeViewModel.path)
            homeViewModel.getResult()

            if (homeViewModel.isResponseSuccessful) {
                if (!homeViewModel.isResponseBodyNullOrError) {
                    homeViewModel.showLoading(false)
                    val moveToResult = Intent(this@HomeActivity, ResultActivity::class.java)
                    moveToResult.putExtra(ResultActivity.URI, homeViewModel.imgUri)
                    moveToResult.putExtra(ResultActivity.RESULT, homeViewModel.result)
                    startActivity(moveToResult)
                }
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            homeViewModel.imgUri = selectedImg.toString()
            val myFile = uriToFile(selectedImg, this@HomeActivity)
            homeViewModel.file = myFile

            homeViewModel.getResult() // agar tidak error
            homeViewModel.showLoading(false)
            val moveToResult = Intent(this@HomeActivity, ResultActivity::class.java)
            moveToResult.putExtra(ResultActivity.URI, homeViewModel.imgUri)
            moveToResult.putExtra(ResultActivity.RESULT, homeViewModel.result)
            startActivity(moveToResult)
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}