package com.alvayonara.movieticket.ui.sign.signup

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alvayonara.movieticket.R
import com.alvayonara.movieticket.ui.home.HomeActivity
import com.alvayonara.movieticket.utils.Preferences
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_sign_up_photoscreen.*
import java.util.*

class SignUpPhotoscreenActivity : AppCompatActivity() {

    private var statusAdd: Boolean = false
    private lateinit var filePath: Uri

    private lateinit var mStorageRef: StorageReference
    private lateinit var preferences: Preferences

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_photoscreen)

        preferences = Preferences(this)

        mStorageRef = FirebaseStorage.getInstance().reference

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
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading...")
            progressDialog.show()

            val ref = mStorageRef.child("images/" + UUID.randomUUID().toString())

            ref.putFile(filePath)
                // If upload photo success
                .addOnSuccessListener {
                    progressDialog.dismiss()

                    Toast.makeText(
                        this@SignUpPhotoscreenActivity,
                        "Success Uploaded",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Set value preferences "url" image uploaded
                    ref.downloadUrl.addOnSuccessListener {
                        preferences.setValues("url", it.toString())
                    }

                    finishAffinity()

                    val intent = Intent(
                        this@SignUpPhotoscreenActivity,
                        HomeActivity::class.java
                    )
                    startActivity(intent)
                }
                // If upload photo fail
                .addOnFailureListener { e ->
                    progressDialog.dismiss()

                    Toast.makeText(
                        this@SignUpPhotoscreenActivity,
                        "Upload Failed" + e.message,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                // Show upload photo progress in %
                .addOnProgressListener { taskSnapshot ->
                    val progress =
                        100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount

                    progressDialog.setMessage("Uploaded " + progress.toInt() + "%")
                }
        }

        btn_home.setOnClickListener {
            finishAffinity()

            val intent = Intent(this@SignUpPhotoscreenActivity, HomeActivity::class.java)
            startActivity(intent)
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
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }
}