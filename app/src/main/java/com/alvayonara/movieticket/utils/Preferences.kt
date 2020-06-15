package com.alvayonara.movieticket.utils

import android.content.Context
import android.content.SharedPreferences

class Preferences(context: Context) {

    companion object {
        const val MOVIE_PREF = "USER_PREF"
    }

    private val sharedPref = context.getSharedPreferences(MOVIE_PREF, 0)

    fun setValues(key: String, value: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getValues(key: String): String? {
        return sharedPref.getString(key, null)
    }
}