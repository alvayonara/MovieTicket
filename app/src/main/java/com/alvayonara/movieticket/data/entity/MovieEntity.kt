package com.alvayonara.movieticket.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieEntity(
    var title: String? = "",
    var description: String? = "",
    var genre: String? = "",
    var poster: String? = "",
    var rating: String? = "",
    var backdrop: String? = ""
) : Parcelable