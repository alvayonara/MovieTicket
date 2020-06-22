package com.alvayonara.movieticket.ui.sign.signin

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alvayonara.movieticket.R
import com.alvayonara.movieticket.ui.home.HomeActivity
import com.alvayonara.movieticket.ui.sign.signup.SignUpActivity
import com.alvayonara.movieticket.utils.Preferences
import com.alvayonara.movieticket.utils.PreferencesKey.BALANCE
import com.alvayonara.movieticket.utils.PreferencesKey.EMAIL
import com.alvayonara.movieticket.utils.PreferencesKey.NAME
import com.alvayonara.movieticket.utils.PreferencesKey.ON_BOARDING
import com.alvayonara.movieticket.utils.PreferencesKey.PASSWORD
import com.alvayonara.movieticket.utils.PreferencesKey.STATUS
import com.alvayonara.movieticket.utils.PreferencesKey.UID
import com.alvayonara.movieticket.utils.PreferencesKey.URL
import com.alvayonara.movieticket.utils.ToolbarConfig
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_sign_in.*


class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference

    private lateinit var signInViewModel: SignInViewModel

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

        signInViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[SignInViewModel::class.java]

        // Initialize Shared Preferences
        preferences = Preferences(this)

        // Set preferences onboarding to 1 (true)
        preferences.setValues(ON_BOARDING, "1")

        checkLoginStatus()

        initView()
    }

    private fun initToolbar() {
        ToolbarConfig.setDefaultStatusBarColor(this)
    }

    private fun checkLoginStatus() {
        // Check preferences login status
        // Status 1 -> Already logged in
        // Status 0 -> Not logged in yet
        if (preferences.getValues(STATUS).equals("1")) {

            val intent = Intent(this@SignInActivity, HomeActivity::class.java)
            startActivity(intent)

            finish()
        }
    }

    private fun initView() {
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

    private fun performLogin(email: String, password: String) {
        signInViewModel.setSignIn(mDatabase, auth, email, password)
            .observe(this, Observer { signIn ->
                if (signIn != null) {
                    // Set value logged user information and login status to 1 (true)
                    preferences.setValues(STATUS, "1")
                    preferences.setValues(UID, auth.uid.toString())
                    preferences.setValues(EMAIL, signIn.email.toString())
                    preferences.setValues(PASSWORD, signIn.password.toString())
                    preferences.setValues(NAME, signIn.name.toString())
                    preferences.setValues(URL, signIn.url.toString())
                    preferences.setValues(BALANCE, signIn.balance.toString())

                    val intent = Intent(this@SignInActivity, HomeActivity::class.java)
                    startActivity(intent)

                    finish()
                } else {
                    Toast.makeText(
                        this@SignInActivity,
                        "Wrong email or password",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            })
    }
}