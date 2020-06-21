package com.alvayonara.movieticket.ui.detailmovie

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alvayonara.movieticket.R
import com.alvayonara.movieticket.data.entity.MovieEntity
import com.alvayonara.movieticket.ui.chooseseat.ChooseSeatActivity
import com.alvayonara.movieticket.ui.chooseseat.ChooseSeatActivity.Companion.EXTRA_MOVIE_DATA
import com.alvayonara.movieticket.ui.dashboard.PlaysAdapter
import com.alvayonara.movieticket.utils.ToolbarConfig
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_detail_movie.*

class DetailMovieActivity : AppCompatActivity() {

    private lateinit var mDatabase: DatabaseReference
    private lateinit var detailMovieViewModel: DetailMovieViewModel

    private lateinit var playsAdapter: PlaysAdapter

    companion object {
        const val EXTRA_MOVIE = "extra_movie"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)

        initToolbar()

        // Parcelable extra from previous activity
        val movie = intent.getParcelableExtra(EXTRA_MOVIE) as MovieEntity

        // Initialize Firebase
        mDatabase =
            FirebaseDatabase.getInstance().getReference("Movie").child(movie.title.toString())
                .child("play")

        // Initialize View Model
        detailMovieViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[DetailMovieViewModel::class.java]

        playsAdapter = PlaysAdapter()

        initView(movie, mDatabase, detailMovieViewModel)
    }

    private fun initToolbar() {
        ToolbarConfig.setTransparentStatusBar(this)
    }

    private fun initView(
        movie: MovieEntity,
        mDatabase: DatabaseReference,
        detailMovieViewModel: DetailMovieViewModel
    ) {
        tv_title.text = movie.title
        tv_genre.text = movie.genre

        if (movie.rating != "0") {
            tv_rate.text = movie.rating
        } else {
            tv_rate.text = "n/a"
        }

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

        detailMovieViewModel.getPlaysMovie(mDatabase).observe(this, Observer { credits ->
            playsAdapter.setPlays(credits)
            playsAdapter.notifyDataSetChanged()
        })

        with(rv_play) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = playsAdapter
        }
    }
}