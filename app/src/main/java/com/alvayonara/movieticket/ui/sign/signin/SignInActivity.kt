package com.alvayonara.movieticket.ui.sign.signin

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alvayonara.movieticket.R
import com.alvayonara.movieticket.ui.home.HomeActivity
import com.alvayonara.movieticket.ui.sign.signup.SignUpActivity
import com.alvayonara.movieticket.utils.Preferences
import com.alvayonara.movieticket.utils.ToolbarConfig
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_sign_in.*


class SignInActivity : AppCompatActivity() {

    private lateinit var mDatabase: DatabaseReference
    private lateinit var preferences: Preferences

    private lateinit var username: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        initToolbar()

        // Initialize Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference("user")

        // Initialize Shared Preferences
        preferences = Preferences(this)

        // Set preferences onboarding to 1 (true)
        preferences.setValues("onboarding", "1")

        checkLoginStatus()

        btn_sign_in.setOnClickListener {
            username = edt_username.text.trim().toString()
            password = edt_password.text.trim().toString()

            when {
                TextUtils.isEmpty(username) -> {
                    edt_username.error = "Username empty"
                    edt_username.requestFocus()
                }
                TextUtils.isEmpty(password) -> {
                    edt_password.error = "Password empty"
                    edt_password.requestFocus()
                }
                else -> {
                    pushLogin(username, password)
                }
            }
        }

        btn_sign_up.setOnClickListener {
            val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initToolbar() {
        ToolbarConfig.setDefaultStatusBarColor(this)
    }

    private fun checkLoginStatus() {
        // Check preferences login status
        // Status 1 -> Already logged in
        // Status 0 -> Not logged in yet
        if (preferences.getValues("status").equals("1")) {
            finishAffinity()

            val intent = Intent(this@SignInActivity, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun pushLogin(username: String, password: String) {
        mDatabase.child(username).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d("Error Login: ", p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(User::class.java)

                if (user == null) {
                    Toast.makeText(this@SignInActivity, "User not found!", Toast.LENGTH_LONG).show()
                } else {
                    if (user.password.equals(password)) {

                        // Set value logged user information and login status to 1 (true)
                        preferences.setValues("status", "1")
                        preferences.setValues("username", user.username.toString())
                        preferences.setValues("password", user.password.toString())
                        preferences.setValues("name", user.name.toString())
                        preferences.setValues("email", user.email.toString())
                        preferences.setValues("url", user.url.toString())
                        preferences.setValues("balance", user.balance.toString())

                        finishAffinity()

                        val intent = Intent(this@SignInActivity, HomeActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@SignInActivity, "Wrong password", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        })
    }
}