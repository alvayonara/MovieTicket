package com.alvayonara.movieticket.ui.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.alvayonara.movieticket.R
import com.alvayonara.movieticket.ui.onboarding.OnboardingOneActivity
import com.alvayonara.movieticket.utils.ToolbarConfig

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        initToolbar()

        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(this@SplashScreenActivity, OnboardingOneActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }

    private fun initToolbar() {
        ToolbarConfig.setDefaultStatusBarColor(this)
    }
}