package com.alvayonara.movieticket.ui.detailmovie

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alvayonara.movieticket.data.entity.MovieEntity
import com.alvayonara.movieticket.data.entity.PlaysEntity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class DetailMovieViewModel() : ViewModel() {

    internal fun getPlaysMovie(mDatabase: DatabaseReference): LiveData<List<PlaysEntity>> {
        val dataList = mutableListOf<PlaysEntity>()
        val playResults = MutableLiveData<List<PlaysEntity>>()

        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d("Error get movies data: ", p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (dataSnapshot in p0.children) {
                    val plays = dataSnapshot.getValue(PlaysEntity::class.java)

                    dataList.add(plays!!)
                }
                playResults.postValue(dataList)
            }
        })

        return playResults
    }
}