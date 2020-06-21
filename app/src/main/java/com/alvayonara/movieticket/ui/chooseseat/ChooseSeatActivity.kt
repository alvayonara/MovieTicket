package com.alvayonara.movieticket.ui.chooseseat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.alvayonara.movieticket.R
import com.alvayonara.movieticket.data.entity.CheckoutEntity
import com.alvayonara.movieticket.data.entity.MovieEntity
import com.alvayonara.movieticket.ui.checkout.CheckoutActivity
import com.alvayonara.movieticket.ui.checkout.CheckoutActivity.Companion.EXTRA_CHECKOUT
import com.alvayonara.movieticket.ui.checkout.CheckoutActivity.Companion.EXTRA_MOVIE_CHECKOUT
import com.alvayonara.movieticket.utils.ToolbarConfig
import kotlinx.android.synthetic.main.activity_choose_seat.*
import java.util.ArrayList

class ChooseSeatActivity : AppCompatActivity() {

    private var dataList = ArrayList<CheckoutEntity>()

    private var seatA4Availability: Boolean = false
    private var seatC2Availability: Boolean = false
    private var seatD2Availability: Boolean = false

    private var totalPurchaseTicket: Int = 0

    companion object {
        const val EXTRA_MOVIE_DATA = "extra_movie_data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_seat)

        initToolbar()

        // Parcelable extra from previous activity
        val movie = intent.getParcelableExtra(EXTRA_MOVIE_DATA) as MovieEntity

        initView(movie)
    }

    private fun initToolbar() {
        ToolbarConfig.setDefaultStatusBarColor(this)
    }

    private fun initView(movie: MovieEntity) {
        tv_title.text = movie.title

        seat_a4.setOnClickListener {
            // If not selecting seat
            if (seatA4Availability) {
                // Set seat to empty (no selected)
                seat_a4.setImageResource(R.drawable.ic_rectangle_empty)

                // Set status seat to false
                seatA4Availability = false

                // Decrement total purchase ticket this seat
                totalPurchaseTicket -= 1

                // Show button purchase ticket
                initPurchaseTicket(totalPurchaseTicket)

                // Delete seat data from ArrayList
                dataList.remove(CheckoutEntity("A4", "50000"))
            } else {
                // If selecting this seat

                // Set seat selected
                seat_a4.setImageResource(R.drawable.ic_rectangle_selected)

                // Set status seat to true
                seatA4Availability = true

                // Increment total purchase ticket this seat
                totalPurchaseTicket += 1

                // Show button purchase ticket
                initPurchaseTicket(totalPurchaseTicket)

                // Add seat data to ArrayList
                dataList.add(CheckoutEntity("A4", "50000"))
            }
        }

        seat_c2.setOnClickListener {
            if (seatC2Availability) {
                seat_c2.setImageResource(R.drawable.ic_rectangle_empty)
                seatC2Availability = false
                totalPurchaseTicket -= 1
                initPurchaseTicket(totalPurchaseTicket)

                dataList.remove(CheckoutEntity("C2", "50000"))
            } else {
                seat_c2.setImageResource(R.drawable.ic_rectangle_selected)
                seatC2Availability = true
                totalPurchaseTicket += 1
                initPurchaseTicket(totalPurchaseTicket)

                dataList.add(CheckoutEntity("C2", "50000"))
            }
        }

        seat_d2.setOnClickListener {
            if (seatD2Availability) {
                seat_d2.setImageResource(R.drawable.ic_rectangle_empty)
                seatD2Availability = false
                totalPurchaseTicket -= 1
                initPurchaseTicket(totalPurchaseTicket)

                dataList.remove(CheckoutEntity("D2", "50000"))
            } else {
                seat_d2.setImageResource(R.drawable.ic_rectangle_selected)
                seatD2Availability = true
                totalPurchaseTicket += 1
                initPurchaseTicket(totalPurchaseTicket)

                dataList.add(CheckoutEntity("D2", "50000"))
            }
        }

        iv_back.setOnClickListener {
            finish()
        }

        btn_purchase.setOnClickListener {
            val intent = Intent(this@ChooseSeatActivity, CheckoutActivity::class.java).apply {
                putExtra(EXTRA_MOVIE_CHECKOUT, movie)
                putExtra(EXTRA_CHECKOUT, dataList)
            }
            startActivity(intent)
        }
    }

    private fun initPurchaseTicket(totalPurchaseTicket: Int) {
        if (totalPurchaseTicket == 0) {
            btn_purchase.text = R.string.purchase_ticket.toString()
            btn_purchase.visibility = View.INVISIBLE
        } else {
            btn_purchase.text = "Purchase Ticket ($totalPurchaseTicket)"
            btn_purchase.visibility = View.VISIBLE
        }
    }
}