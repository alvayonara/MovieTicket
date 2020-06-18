package com.alvayonara.movieticket.ui.checkout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alvayonara.movieticket.R
import com.alvayonara.movieticket.data.entity.CheckoutEntity
import com.alvayonara.movieticket.data.entity.MovieEntity
import kotlinx.android.synthetic.main.row_item_checkout.view.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class CheckoutAdapter : RecyclerView.Adapter<CheckoutAdapter.CheckoutViewHolder>() {

    private var listCheckouts = ArrayList<CheckoutEntity>()

    fun setCheckouts(checkouts: List<CheckoutEntity>?) {
        if (checkouts == null) return
        listCheckouts.clear()
        listCheckouts.addAll(checkouts)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CheckoutViewHolder = CheckoutViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.row_item_checkout, parent, false)
    )

    override fun getItemCount(): Int = listCheckouts.size

    override fun onBindViewHolder(holder: CheckoutViewHolder, position: Int) =
        holder.bindItem(listCheckouts[position])

    class CheckoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(checkout: CheckoutEntity) {
            with(itemView) {
                // Recyclerview data conditional
                // * If recyclerview data seat contains text "Total"
                if (checkout.seat!!.startsWith("Total")) {
                    tv_seat.text = checkout.seat
                    tv_seat.setCompoundDrawables(null, null, null, null)
                } else {
                    // * Else if recyclerview data seat not contains text "Total"
                    tv_seat.text = "Seat (${checkout.seat})"
                }

                // Convert currency ticket price to Rupiah
                val localeID = Locale("in", "ID")
                val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
                tv_price.text = formatRupiah.format(checkout.price!!.toDouble())
            }
        }
    }
}