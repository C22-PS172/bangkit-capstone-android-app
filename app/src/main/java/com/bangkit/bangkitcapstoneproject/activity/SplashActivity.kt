package com.bangkit.bangkitcapstoneproject.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bangkit.bangkitcapstoneproject.activity.ui.home.HomeActivity
import com.bangkit.bangkitcapstoneproject.databinding.ActivitySplashBinding
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
        val appIcon = ObjectAnimator.ofFloat(binding.appIcon, View.ALPHA, 1f).setDuration(500)
        val appName = ObjectAnimator.ofFloat(binding.appName, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(appIcon, appName)
            startDelay = 500
            start()
        }
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