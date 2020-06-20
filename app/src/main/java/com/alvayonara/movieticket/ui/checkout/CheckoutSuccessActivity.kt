package com.alvayonara.movieticket.ui.checkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alvayonara.movieticket.R
import com.alvayonara.movieticket.ui.home.HomeActivity
import com.alvayonara.movieticket.ui.ticket.TicketDetailActivity
import com.alvayonara.movieticket.ui.ticket.TicketFragment
import com.alvayonara.movieticket.utils.ToolbarConfig
import kotlinx.android.synthetic.main.activity_checkout_success.*

class CheckoutSuccessActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout_success)

        initToolbar()

        btn_home.setOnClickListener {
            finishAffinity()

            val intent = Intent(
                this@CheckoutSuccessActivity,
                HomeActivity::class.java
            )
            startActivity(intent)
        }
    }

    private fun initToolbar() {
        ToolbarConfig.setDefaultStatusBarColor(this)
    }
}