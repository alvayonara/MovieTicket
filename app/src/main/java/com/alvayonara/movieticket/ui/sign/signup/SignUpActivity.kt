package com.alvayonara.movieticket.ui.sign.signup

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alvayonara.movieticket.R
import com.alvayonara.movieticket.ui.sign.signin.User
import com.alvayonara.movieticket.utils.Preferences
import com.alvayonara.movieticket.utils.PreferencesKey.BALANCE
import com.alvayonara.movieticket.utils.PreferencesKey.EMAIL
import com.alvayonara.movieticket.utils.PreferencesKey.NAME
import com.alvayonara.movieticket.utils.PreferencesKey.PASSWORD
import com.alvayonara.movieticket.utils.PreferencesKey.STATUS
import com.alvayonara.movieticket.utils.PreferencesKey.UID
import com.alvayonara.movieticket.utils.PreferencesKey.URL
import com.alvayonara.movieticket.utils.ToolbarConfig
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.net.URI

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var mDatabase: DatabaseReference
    private lateinit var preferences: Preferences

    private lateinit var signUpViewModel: SignUpViewModel

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

        signUpViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[SignUpViewModel::class.java]

        // Initialize Shared Preferences
        preferences = Preferences(this)

        initView()
    }

    private fun initView(
    ) {
        img_arrow_back.setOnClickListener {
            finish()
        }

        btn_sign_up.setOnClickListener {
            password = edt_password.text.trim().toString()
            name = edt_name.text.toString()
            email = edt_email.text.trim().toString()

            when {
                TextUtils.isEmpty(email) -> {
                    edt_email.error = "Email empty"
                    edt_email.requestFocus()
                }
                !isValidEmail(email) -> {
                    edt_email.error = "Email not valid"
                    edt_email.requestFocus()
                }
                TextUtils.isEmpty(password) -> {
                    edt_password.error = "Password empty"
                    edt_password.requestFocus()
                }
                password.length < 6 -> {
                    edt_password.error = "Password minimum contain 6 character"
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

                    performSignUp(user)
                }
            }
        }
    }

    private fun initToolbar() = ToolbarConfig.setDefaultStatusBarColor(this)

    private fun isValidEmail(email: CharSequence): Boolean =
        android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun performSignUp(user: User) {
        signUpViewModel.setSignUp(mDatabase, auth, user).observe(this, Observer { signUp ->
            if (signUp != null) {
                // Set value logged user information and login status to 1 (true)
                preferences.setValues(STATUS, "1")
                preferences.setValues(UID, auth.uid.toString())
                preferences.setValues(EMAIL, signUp.email.toString())
                preferences.setValues(PASSWORD, signUp.password.toString())
                preferences.setValues(NAME, signUp.name.toString())
                preferences.setValues(URL, "")
                preferences.setValues(BALANCE, "")

                val intent = Intent(
                    this@SignUpActivity,
                    SignUpPhotoscreenActivity::class.java
                ).putExtra("name", signUp.name)
                startActivity(intent)

                finish()
            } else {
                Toast.makeText(
                    this@SignUpActivity,
                    "Email already registered",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        })
    }
}