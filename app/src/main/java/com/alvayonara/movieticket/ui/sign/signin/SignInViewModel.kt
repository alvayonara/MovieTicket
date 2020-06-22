package com.alvayonara.movieticket.ui.sign.signin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class SignInViewModel : ViewModel() {

    internal fun setSignIn(
        mDatabaseReference: DatabaseReference,
        auth: FirebaseAuth,
        email: String,
        password: String
    ): LiveData<User> {
        val signInResult = MutableLiveData<User>()

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                mDatabaseReference.child(auth.uid.toString())
                    .addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            Log.d("Error Get Data: ", p0.message)
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            val user = p0.getValue(User::class.java)

                            signInResult.postValue(user)
                        }
                    })
            } else {
                signInResult.postValue(null)
            }
        }

        return signInResult
    }
}