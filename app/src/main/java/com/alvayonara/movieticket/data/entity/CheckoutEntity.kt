package com.alvayonara.movieticket.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CheckoutEntity(
    var seat: String? = "",
    var price: String? = ""
) : Parcelable