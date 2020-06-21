package com.alvayonara.movieticket.ui.sign.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.alvayonara.movieticket.R
import com.alvayonara.movieticket.ui.sign.signin.User
import com.alvayonara.movieticket.utils.Preferences
import com.alvayonara.movieticket.utils.ToolbarConfig
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.w3c.dom.Text

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var mDatabase: DatabaseReference
    private lateinit var preferences: Preferences

    private lateinit var email: String
    private lateinit var password: String
    private lateinit var name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        initToolbar()

        // Initialize Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference("user")

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Initialize Shared Preferences
        preferences = Preferences(this)

        btn_sign_up.setOnClickListener {
            password = edt_password.text.trim().toString()
            name = edt_name.text.toString()
            email = edt_email.text.trim().toString()

            when {
                TextUtils.isEmpty(email) -> {
                    edt_email.error = "Email empty"
                    edt_email.requestFocus()
                }
                TextUtils.isEmpty(password) -> {
                    edt_password.error = "Password Empty"
                    edt_password.requestFocus()
                }
                TextUtils.isEmpty(name) -> {
                    edt_name.error = "Name empty"
                    edt_name.requestFocus()
                }
                else -> {
                    val user = User()

                    user.email = email
                    user.password = password
                    user.name = name

                    registerAccount(user)
                }
            }
        }
    }

    private fun initToolbar() {
        ToolbarConfig.setDefaultStatusBarColor(this)
    }

    private fun registerAccount(user: User) {
        auth.createUserWithEmailAndPassword(user.email!!, user.password!!)
            .addOnCompleteListener(this, OnCompleteListener { task ->
                if (task.isSuccessful) {

                    mDatabase.child(auth.uid.toString()).setValue(user)

                    // Set value logged user information and login status to 1 (true)
                    preferences.setValues("status", "1")
                    preferences.setValues("uid", auth.uid.toString())
                    preferences.setValues("email", user.email.toString())
                    preferences.setValues("password", user.password.toString())
                    preferences.setValues("name", user.name.toString())
                    preferences.setValues("url", "")
                    preferences.setValues("balance", "")

                    val intent = Intent(
                        this@SignUpActivity,
                        SignUpPhotoscreenActivity::class.java
                    ).putExtra("name", user.name)
                    startActivity(intent)

                    finish()
                } else {
                    Log.d("errsign", task.exception.toString())
                    Toast.makeText(
                        this@SignUpActivity,
                        "Username already exist!",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            })
    }
}