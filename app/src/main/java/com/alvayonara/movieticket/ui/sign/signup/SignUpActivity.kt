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
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.w3c.dom.Text

class SignUpActivity : AppCompatActivity() {

    private lateinit var mDatabase: DatabaseReference
    private lateinit var preferences: Preferences

    private lateinit var username: String
    private lateinit var password: String
    private lateinit var name: String
    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        initToolbar()

        // Initialize Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference("user")

        // Initialize Shared Preferences
        preferences = Preferences(this)

        btn_sign_up.setOnClickListener {
            username = edt_username.text.trim().toString()
            password = edt_password.text.trim().toString()
            name = edt_name.text.toString()
            email = edt_email.text.trim().toString()

            when {
                TextUtils.isEmpty(username) -> {
                    edt_username.error = "Username empty"
                    edt_username.requestFocus()
                }
                TextUtils.isEmpty(password) -> {
                    edt_password.error = "Password Empty"
                    edt_password.requestFocus()
                }
                TextUtils.isEmpty(name) -> {
                    edt_name.error = "Name empty"
                    edt_name.requestFocus()
                }
                TextUtils.isEmpty(email) -> {
                    edt_email.error = "Email empty"
                    edt_email.requestFocus()
                }
                else -> {
                    val user = User()

                    user.username = username
                    user.password = password
                    user.name = name
                    user.email = email

                    registerAccount(user)
                }
            }
        }
    }

    private fun initToolbar() {
        ToolbarConfig.setDefaultStatusBarColor(this)
    }

    private fun registerAccount(userData: User) {
        mDatabase.child(userData.username.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Log.d("Error Register: ", p0.message)
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val user = p0.getValue(User::class.java)

                    if (user == null) {
                        mDatabase.child(userData.username.toString()).setValue(userData)

                        // Set value logged user information and login status to 1 (true)
                        preferences.setValues("status", "1")
                        preferences.setValues("username", userData.username.toString())
                        preferences.setValues("password", userData.password.toString())
                        preferences.setValues("name", userData.name.toString())
                        preferences.setValues("email", userData.email.toString())
                        preferences.setValues("url", "")
                        preferences.setValues("balance", "500000")

                        finishAffinity()

                        val intent = Intent(
                            this@SignUpActivity,
                            SignUpPhotoscreenActivity::class.java
                        ).putExtra("name", userData.name)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this@SignUpActivity,
                            "Username already exist!",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }
            })
    }
}