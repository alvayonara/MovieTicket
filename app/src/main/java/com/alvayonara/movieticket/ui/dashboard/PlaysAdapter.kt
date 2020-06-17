package com.alvayonara.movieticket.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alvayonara.movieticket.R
import com.alvayonara.movieticket.data.entity.PlaysEntity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.row_item_play.view.*

class PlaysAdapter : RecyclerView.Adapter<PlaysAdapter.PlaysViewHolder>() {

    private var listPlays = ArrayList<PlaysEntity>()

    fun setPlays(plays: List<PlaysEntity>?) {
        if (plays == null) return
        listPlays.clear()
        listPlays.addAll(plays)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaysViewHolder = PlaysViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.row_item_play, parent, false)
    )

    override fun getItemCount(): Int = listPlays.size

    override fun onBindViewHolder(holder: PlaysAdapter.PlaysViewHolder, position: Int) {
        holder.bindItem(listPlays[position])
    }

    class PlaysViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(play: PlaysEntity) {
            with(itemView) {
                tv_name_play.text = play.name

                Glide.with(context)
                    .load(play.url)
                    .apply(RequestOptions.circleCropTransform())
                    .into(iv_poster_image)
            }
        }
    }
}