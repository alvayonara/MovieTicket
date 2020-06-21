package com.alvayonara.movieticket.ui.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alvayonara.movieticket.R
import com.alvayonara.movieticket.ui.sign.signin.SignInActivity
import com.alvayonara.movieticket.utils.Preferences
import com.alvayonara.movieticket.utils.ToolbarConfig
import kotlinx.android.synthetic.main.activity_onboarding_one.*

class OnboardingOneActivity : AppCompatActivity() {

    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_one)

        initToolbar()

        // Initialize Shared Preferences
        preferences = Preferences(this)

        checkOnboardingStatus()

        btn_home.setOnClickListener {
            val intent = Intent(this@OnboardingOneActivity, OnboardingTwoActivity::class.java)
            startActivity(intent)
        }

        btn_skip.setOnClickListener {
            // Set flag onboarding to 1 (skip onboarding) in Preferences
            preferences.setValues("onboarding", "1")

            val intent = Intent(this@OnboardingOneActivity, SignInActivity::class.java)
            startActivity(intent)

            finish()
        }
    }

    private fun initToolbar() {
        ToolbarConfig.setDefaultStatusBarColor(this)
    }

    private fun checkOnboardingStatus() {
        // Check preferences Onboarding status
        // onboarding -> 1 (Onboarding skipped to SignInActivity)
        // onboarding -> null (launch from Onboarding)
        if (preferences.getValues("onboarding").equals("1")) {

            val intent = Intent(this@OnboardingOneActivity,
                SignInActivity::class.java)
            startActivity(intent)

            finish()
        }
    }
}