package com.alvayonara.movieticket.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlaysEntity(
    var name: String? = "",
    var url: String? = ""
) : Parcelable