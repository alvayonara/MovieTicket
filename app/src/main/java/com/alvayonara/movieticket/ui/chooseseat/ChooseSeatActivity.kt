package com.alvayonara.movieticket.ui.chooseseat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alvayonara.movieticket.R

class ChooseSeatActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_MOVIE_DATA = "extra_movie_data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_seat)
    }
}