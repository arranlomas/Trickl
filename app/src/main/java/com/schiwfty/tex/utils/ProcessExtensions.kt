package com.schiwfty.tex.utils

/**
 * Created by arran on 17/04/2017.
 */
import android.util.Log


fun Process.captureOutput() {
    val t = Thread {
        errorStream.bufferedReader().use {
            Log.v("ERROR", "value: " + it.readText())
        }

        inputStream.bufferedReader().use {
            Log.v("STD OUT", "value: " + it.readText())
        }
    }
    t.start()
}