package com.alvayonara.movieticket.ui.sign.signup

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.alvayonara.movieticket.R
import androidx.lifecycle.Observer
import com.alvayonara.movieticket.ui.home.HomeActivity
import com.alvayonara.movieticket.utils.Preferences
import com.alvayonara.movieticket.utils.PreferencesKey.UID
import com.alvayonara.movieticket.utils.PreferencesKey.URL
import com.alvayonara.movieticket.utils.ToolbarConfig
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_sign_up_photoscreen.*
import java.util.*

class SignUpPhotoscreenActivity : AppCompatActivity() {

    private var statusAdd: Boolean = false
    private lateinit var filePath: Uri

    private lateinit var mDatabase: DatabaseReference
    private lateinit var mStorageRef: StorageReference

    private lateinit var signUpViewModel: SignUpViewModel

    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_photoscreen)

        initToolbar()

        preferences = Preferences(this)

        // Initialize Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference("user")

        // Initialize Firebase Storage
        mStorageRef = FirebaseStorage.getInstance().reference

        signUpViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[SignUpViewModel::class.java]

        initView()
    }

    private fun initToolbar() {
        ToolbarConfig.setDefaultStatusBarColor(this)
    }

    private fun initView() {
        tv_hello.text = "Selamat Datang\n" + intent.getStringExtra("name")

        iv_add.setOnClickListener {
            if (statusAdd) {
                statusAdd = false

                btn_save.visibility = View.INVISIBLE
                iv_add.setImageResource(R.drawable.ic_btn_upload)
                iv_profile.setImageResource(R.drawable.user_photo_empty)
            } else {
                ImagePicker.with(this)
                    .cameraOnly()
                    .start()
            }
        }

        btn_save.setOnClickListener {
            signUpViewModel.setSignUpPhotoScreen(
                mDatabase,
                mStorageRef,
                preferences.getValues(UID).toString(),
                filePath
            ).observe(this, Observer { url ->
                if (url != null) {
                    preferences.setValues(URL, url)

                    val intent = Intent(
                        this@SignUpPhotoscreenActivity,
                        HomeActivity::class.java
                    )
                    startActivity(intent)

                    finishAffinity()
                } else {
                    Toast.makeText(
                        this@SignUpPhotoscreenActivity,
                        "Upload Failed",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            })
        }

        btn_home.setOnClickListener {
            val intent = Intent(this@SignUpPhotoscreenActivity, HomeActivity::class.java)
            startActivity(intent)

            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            Activity.RESULT_OK -> {
                //Image Uri will not be null for RESULT_OK
                statusAdd = true

                filePath = data?.data!!

                Glide.with(this)
                    .load(filePath)
                    .apply(RequestOptions.circleCropTransform())
                    .into(iv_profile)

                btn_save.visibility = View.VISIBLE
                iv_add.setImageResource(R.drawable.ic_btn_delete)
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }
}