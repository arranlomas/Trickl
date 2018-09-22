package com.shwifty.tex.utils

import android.content.Context
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability

/**
 * Created by arran on 18/10/2017.
 */
fun Context.isChromecastAvailable(): Boolean {
    val status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
    if (status == ConnectionResult.SUCCESS) return true
    return false
}


fun Long.convertToHumanTime(): String {
    val seconds = (this / 1000).toInt() % 60
    val minutes = (this / (1000 * 60) % 60).toInt()
    val hours = (this / (1000 * 60 * 60) % 24).toInt()

    return "$hours:$minutes:$seconds"
}