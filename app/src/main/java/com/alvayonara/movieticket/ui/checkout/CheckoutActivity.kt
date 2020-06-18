package com.alvayonara.movieticket.ui.checkout

import android.content.Intent
import android.graphics.Movie
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alvayonara.movieticket.R
import com.alvayonara.movieticket.data.entity.CheckoutEntity
import com.alvayonara.movieticket.data.entity.MovieEntity
import com.alvayonara.movieticket.ui.chooseseat.ChooseSeatActivity.Companion.EXTRA_MOVIE_DATA
import com.alvayonara.movieticket.ui.detailmovie.DetailMovieActivity
import com.alvayonara.movieticket.utils.Preferences
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.activity_checkout.tv_balance
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class CheckoutActivity : AppCompatActivity() {

    private lateinit var preferences: Preferences

    private var totalTicketPrices: Int = 0

    companion object {
        const val EXTRA_MOVIE_CHECKOUT = "extra_movie_checkout"
        const val EXTRA_CHECKOUT = "extra_checkout"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        // Initialize Shared Preferences
        preferences = Preferences(this)

        // Parcelable extra from previous activity
        val movie = intent.getParcelableExtra(EXTRA_MOVIE_CHECKOUT) as MovieEntity
        val dataList = intent.getSerializableExtra(EXTRA_CHECKOUT) as ArrayList<CheckoutEntity>

        initView(movie, dataList)
    }

    private fun initView(movie: MovieEntity, dataList: ArrayList<CheckoutEntity>) {
        // Insert total prices to recyclerview
        insertTotalPriceData(dataList)

        iv_back.setOnClickListener {
            finish()
        }

        // Checking balance
        if (!preferences.getValues("balance").isNullOrEmpty()) {
            convertCurrencyBalance(preferences.getValues("balance")!!.toDouble())

            tv_insufficient_balance.visibility = View.INVISIBLE
            btn_purchase.visibility = View.VISIBLE
        } else {
            tv_balance.text = getString(R.string.empty_balance)
            tv_insufficient_balance.visibility = View.VISIBLE
            btn_purchase.visibility = View.INVISIBLE
        }

        btn_purchase.setOnClickListener {
            val intent = Intent(this@CheckoutActivity, CheckoutSuccessActivity::class.java)
            startActivity(intent)

            showTicketPurchasedNotification(movie)
        }

        btn_cancel_purchase.setOnClickListener {
            finish()
        }
    }

    private fun insertTotalPriceData(dataList: ArrayList<CheckoutEntity>) {
        // Calculate total ticket price
        for (total in dataList.indices) {
            totalTicketPrices += dataList[total].price!!.toInt()
        }
        // Add total price to recyclerview data
        dataList.add(CheckoutEntity("Total to be paid", totalTicketPrices.toString()))

        // Initialize adapter
        val checkoutAdapter = CheckoutAdapter()

        // Insert recyclerview data (total price) to adapter
        checkoutAdapter.setCheckouts(dataList)

        // Show recyclerview
        with(rv_items_checkout) {
            layoutManager = LinearLayoutManager(this@CheckoutActivity)
            adapter = checkoutAdapter
        }
    }

    private fun convertCurrencyBalance(balance: Double) {
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        tv_balance.text = (formatRupiah.format(balance))
    }

    private fun showTicketPurchasedNotification(movie: MovieEntity) {

    }
}