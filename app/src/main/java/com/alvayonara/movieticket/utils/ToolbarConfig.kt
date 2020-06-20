package com.alvayonara.movieticket.utils

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.alvayonara.movieticket.R

object ToolbarConfig {

    fun setTransparentStatusBar(act: Activity){
        act.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        act.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        act.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        act.window.statusBarColor = Color.TRANSPARENT
    }

    fun setDefaultStatusBarColor(act: Activity) {
        // set status bar color to white
        act.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        act.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        act.window.statusBarColor = ContextCompat.getColor(act, R.color.colorBlue)
    }
}