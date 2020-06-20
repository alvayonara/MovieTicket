package com.alvayonara.movieticket.ui.ticket

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alvayonara.movieticket.R
import com.alvayonara.movieticket.data.entity.CheckoutEntity
import kotlinx.android.synthetic.main.row_item_checkout_white.view.*

class TicketAdapter : RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {

    private var listTickets = ArrayList<CheckoutEntity>()

    fun setTickets(tickets: List<CheckoutEntity>?) {
        if (tickets == null) return
        listTickets.clear()
        listTickets.addAll(tickets)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TicketViewHolder = TicketViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.row_item_checkout_white, parent, false)
    )

    override fun getItemCount(): Int = listTickets.size

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) =
        holder.bindItem(listTickets[position])

    class TicketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(checkoutEntity: CheckoutEntity) {
            with(itemView) {
                tv_seat.text = "Seat No. ${checkoutEntity.seat}"
            }
        }
    }
}