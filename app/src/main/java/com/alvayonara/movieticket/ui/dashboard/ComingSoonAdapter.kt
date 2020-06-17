package com.alvayonara.movieticket.ui.dashboard

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alvayonara.movieticket.R
import com.alvayonara.movieticket.data.entity.MovieEntity
import com.alvayonara.movieticket.ui.detailmovie.DetailMovieActivity
import com.alvayonara.movieticket.ui.detailmovie.DetailMovieActivity.Companion.EXTRA_MOVIE
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.row_item_coming_soon.view.*

class ComingSoonAdapter : RecyclerView.Adapter<ComingSoonAdapter.MovieViewHolder>() {

    private var listMovies = ArrayList<MovieEntity>()

    fun setMovies(movies: List<MovieEntity>?) {
        if (movies == null) return
        listMovies.clear()
        listMovies.addAll(movies)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieViewHolder = MovieViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.row_item_coming_soon, parent, false)
    )

    override fun getItemCount(): Int = listMovies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) =
        holder.bindItem(listMovies[position])

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(movie: MovieEntity) {
            with(itemView) {
                tv_title.text = movie.title
                tv_genre.text = movie.genre
                tv_rate.text = movie.rating
                Glide.with(context)
                    .load(movie.poster)
                    .into(iv_poster_image)

                itemView.setOnClickListener {
                    val intent = Intent(context, DetailMovieActivity::class.java).putExtra(
                        EXTRA_MOVIE,
                        movie
                    )
                    context.startActivity(intent)
                }
            }
        }
    }
}