package com.alvayonara.movieticket.ui.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alvayonara.movieticket.data.entity.MovieEntity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class DashboardViewModel : ViewModel() {

    internal fun getNowPlayingMovies(mDatabase: DatabaseReference): LiveData<List<MovieEntity>> {
        val dataList = mutableListOf<MovieEntity>()
        val movieResults = MutableLiveData<List<MovieEntity>>()

        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d("Error get movies data: ", p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (dataSnapshot in p0.children) {
                    val movies = dataSnapshot.getValue(MovieEntity::class.java)

                    dataList.add(movies!!)
                }
                movieResults.postValue(dataList)
            }
        })

        return movieResults
    }

    internal fun getUpcomingMovies(mDatabase: DatabaseReference): LiveData<List<MovieEntity>> {
        val dataList = mutableListOf<MovieEntity>()
        val movieResults = MutableLiveData<List<MovieEntity>>()

        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d("Error get movies data: ", p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (dataSnapshot in p0.children) {
                    val movies = dataSnapshot.getValue(MovieEntity::class.java)

                    dataList.add(movies!!)
                }
                movieResults.postValue(dataList)
            }
        })

        return movieResults
    }
}