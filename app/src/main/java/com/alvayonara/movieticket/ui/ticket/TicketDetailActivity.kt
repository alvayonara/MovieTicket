package com.alvayonara.movieticket.ui.ticket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alvayonara.movieticket.R
import com.alvayonara.movieticket.data.entity.CheckoutEntity
import com.alvayonara.movieticket.data.entity.MovieEntity
import com.alvayonara.movieticket.ui.detailmovie.DetailMovieActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail_ticket.*

class TicketDetailActivity : AppCompatActivity() {

    private var dataList = ArrayList<CheckoutEntity>()

    companion object {
        const val EXTRA_TICKET = "extra_ticket"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_ticket)

        // Parcelable extra from previous activity
        val movie = intent.getParcelableExtra(EXTRA_TICKET) as MovieEntity

        initView(movie)
    }

    private fun initView(movie: MovieEntity) {
        tv_title.text = movie.title
        tv_genre.text = movie.genre
        tv_rate.text = movie.rating

        Glide.with(this)
            .load(movie.poster)
            .into(iv_poster_image)

        iv_back.setOnClickListener {
            finish()
        }

        dataList.add(CheckoutEntity("A4", ""))
        dataList.add(CheckoutEntity("C2", ""))

        val ticketAdapter = TicketAdapter()

        ticketAdapter.setTickets(dataList)

        with(rv_checkout) {
            layoutManager = LinearLayoutManager(context)
            adapter = ticketAdapter
        }
    }
}