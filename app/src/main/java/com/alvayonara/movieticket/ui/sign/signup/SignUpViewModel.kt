package com.alvayonara.movieticket.ui.sign.signup

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alvayonara.movieticket.ui.sign.signin.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import java.util.*

class SignUpViewModel : ViewModel() {

    internal fun setSignUp(mDatabase: DatabaseReference, auth: FirebaseAuth, user: User): LiveData<User> {
        val signUpResult = MutableLiveData<User>()

        auth.createUserWithEmailAndPassword(user.email!!, user.password!!)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    mDatabase.child(auth.uid.toString()).setValue(user)

                    signUpResult.postValue(user)
                } else {
                    signUpResult.postValue(null)
                }
            }

        return signUpResult
    }

    internal fun setSignUpPhotoScreen(
        mDatabase: DatabaseReference,
        mStorageRef: StorageReference,
        uid: String,
        filePath: Uri
    ): LiveData<String> {
        val signUpPhotoScreenResult = MutableLiveData<String>()
        val ref = mStorageRef.child("images/" + UUID.randomUUID().toString())

        ref.putFile(filePath)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    // Insert url data to user
                    mDatabase.child(uid)
                        .child("url").setValue(it.toString())

                    signUpPhotoScreenResult.postValue(it.toString())
                }
            }
            .addOnFailureListener {
                signUpPhotoScreenResult.postValue(null)
            }

        return signUpPhotoScreenResult
    }
}