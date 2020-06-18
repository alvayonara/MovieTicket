package com.alvayonara.movieticket.ui.checkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alvayonara.movieticket.R
import com.alvayonara.movieticket.ui.dashboard.DashboardFragment
import com.alvayonara.movieticket.ui.home.HomeActivity
import kotlinx.android.synthetic.main.activity_checkout_success.*

class CheckoutSuccessActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout_success)

        btn_ticket.setOnClickListener {

        }

        btn_home.setOnClickListener {
            finishAffinity()

            val intent = Intent(
                this@CheckoutSuccessActivity,
                HomeActivity::class.java
            )
            startActivity(intent)
        }
    }
}