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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_sign_in.*


class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference

    private lateinit var preferences: Preferences

    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        initToolbar()

        // Initialize Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference("user")

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Initialize Shared Preferences
        preferences = Preferences(this)

        // Set preferences onboarding to 1 (true)
        preferences.setValues("onboarding", "1")

        checkLoginStatus()

        btn_sign_in.setOnClickListener {
            email = edt_email.text.trim().toString()
            password = edt_password.text.trim().toString()

            when {
                TextUtils.isEmpty(email) -> {
                    edt_email.error = "Email empty"
                    edt_email.requestFocus()
                }
                TextUtils.isEmpty(password) -> {
                    edt_password.error = "Password empty"
                    edt_password.requestFocus()
                }
                else -> {
                    performLogin(email, password)
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

            val intent = Intent(this@SignInActivity, HomeActivity::class.java)
            startActivity(intent)

            finish()
        }
    }

    private fun performLogin(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, OnCompleteListener { task ->
                if (task.isSuccessful) {
                    getDataLogin()
                } else {
                    Toast.makeText(this@SignInActivity, "Wrong password", Toast.LENGTH_LONG)
                        .show()
                }
            })
    }

    private fun getDataLogin() {
        mDatabase.child(auth.uid.toString()).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d("Error Get Data: ", p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(User::class.java)

                if (user != null) {
                    // Set value logged user information and login status to 1 (true)
                    preferences.setValues("status", "1")
                    preferences.setValues("uid", auth.uid.toString())
                    preferences.setValues("email", user.email.toString())
                    preferences.setValues("password", user.password.toString())
                    preferences.setValues("name", user.name.toString())
                    preferences.setValues("url", user.url.toString())
                    preferences.setValues("balance", user.balance.toString())
                }

                val intent = Intent(this@SignInActivity, HomeActivity::class.java)
                startActivity(intent)

                finish()
            }
        })
    }

//    private fun pushLogin(username: String, password: String) {
//        mDatabase.child(username).addValueEventListener(object : ValueEventListener {
//            override fun onCancelled(p0: DatabaseError) {
//                Log.d("Error Login: ", p0.message)
//            }
//
//            override fun onDataChange(p0: DataSnapshot) {
//                val user = p0.getValue(User::class.java)
//
//                if (user == null) {
//                    Toast.makeText(this@SignInActivity, "User not found!", Toast.LENGTH_LONG).show()
//                } else {
//                    if (user.password.equals(password)) {
//
//                        // Set value logged user information and login status to 1 (true)
//                        preferences.setValues("status", "1")
//                        preferences.setValues("username", user.username.toString())
//                        preferences.setValues("password", user.password.toString())
//                        preferences.setValues("name", user.name.toString())
//                        preferences.setValues("email", user.email.toString())
//                        preferences.setValues("url", user.url.toString())
//                        preferences.setValues("balance", user.balance.toString())
//
//                        finishAffinity()
//
//                        val intent = Intent(this@SignInActivity, HomeActivity::class.java)
//                        startActivity(intent)
//                    } else {
//                        Toast.makeText(this@SignInActivity, "Wrong password", Toast.LENGTH_LONG)
//                            .show()
//                    }
//                }
//            }
//        })
//    }
}