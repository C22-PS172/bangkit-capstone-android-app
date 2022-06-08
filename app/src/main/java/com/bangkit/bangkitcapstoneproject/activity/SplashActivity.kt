package com.bangkit.bangkitcapstoneproject.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.bangkitcapstoneproject.activity.ui.HomeActivity
import com.bangkit.bangkitcapstoneproject.databinding.ActivitySplashBinding
import com.bumptech.glide.Glide
import java.util.*
import kotlin.concurrent.schedule

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        setupView()
        init()
    }

    private fun setupView() {
        Glide.with(this)
            .load("https://i.stack.imgur.com/3hRmg.png")
            .circleCrop()
            .into(binding.appIcon)

    }

    private fun goToHome() {
        val intent = Intent(this@SplashActivity, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun init() {
        Timer().schedule(2000) {
            goToHome()
        }
    }

}