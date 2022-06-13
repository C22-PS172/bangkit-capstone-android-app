package com.bangkit.bangkitcapstoneproject.activity.ui.result

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import com.bangkit.bangkitcapstoneproject.activity.network.Result
import com.bangkit.bangkitcapstoneproject.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imgUri = intent.getStringExtra(URI) as String
        val result = intent.getParcelableExtra<Result>(RESULT) as Result

        binding.resultImage.setImageURI(imgUri.toUri())
        binding.resultName.text = result.name
        binding.resultScientificName.text = result.scientificName
        binding.resultType.text = result.type
    }

    companion object {
        const val URI = "uri"
        const val RESULT = "result"
    }
}