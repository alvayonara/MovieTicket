package com.alvayonara.movieticket.ui.checkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alvayonara.movieticket.R

class CheckoutActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_CHECKOUT = "extra_checkout"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
    }
}