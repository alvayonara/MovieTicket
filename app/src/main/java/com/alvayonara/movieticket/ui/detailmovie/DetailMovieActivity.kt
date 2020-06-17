package com.alvayonara.movieticket.ui.detailmovie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.alvayonara.movieticket.R
import com.alvayonara.movieticket.data.entity.MovieEntity
import com.alvayonara.movieticket.data.entity.PlaysEntity
import com.alvayonara.movieticket.ui.chooseseat.ChooseSeatActivity
import com.alvayonara.movieticket.ui.chooseseat.ChooseSeatActivity.Companion.EXTRA_MOVIE_DATA
import com.alvayonara.movieticket.ui.dashboard.NowPlayingAdapter
import com.alvayonara.movieticket.ui.dashboard.PlaysAdapter
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_detail_movie.*
import java.util.ArrayList

class DetailMovieActivity : AppCompatActivity() {

    private lateinit var mDatabase: DatabaseReference

    private lateinit var playsAdapter: PlaysAdapter

    private var dataList = ArrayList<PlaysEntity>()

    companion object {
        const val EXTRA_MOVIE = "extra_movie"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)

        // Parcelable extra from previous activity
        val movie = intent.getParcelableExtra(EXTRA_MOVIE) as MovieEntity

        // Initialize Firebase
        mDatabase =
            FirebaseDatabase.getInstance().getReference("Movie").child(movie.title.toString())
                .child("play")

        playsAdapter = PlaysAdapter()

        initView(movie)

        getPlaysData()
    }

    private fun initView(movie: MovieEntity) {
        tv_title.text = movie.title
        tv_rate.text = movie.rating
        tv_genre.text = movie.genre
        tv_description.text = movie.description

        Glide.with(this)
            .load(movie.backdrop)
            .into(iv_poster)

        iv_back.setOnClickListener {
            finish()
        }

        btn_choose_seat.setOnClickListener {
            val intent =
                Intent(this@DetailMovieActivity, ChooseSeatActivity::class.java).putExtra(
                    EXTRA_MOVIE_DATA,
                    movie
                )
            startActivity(intent)
        }
    }

    private fun getPlaysData() {
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d("Error get plays data: ", p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (dataSnapshot in p0.children) {
                    val play = dataSnapshot.getValue(PlaysEntity::class.java)

                    dataList.add(play!!)
                }

                playsAdapter.setPlays(dataList)

                with(rv_play) {
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    adapter = playsAdapter
                }
            }
        })
    }
}