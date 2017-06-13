package com.shwifty.tex.utils

import android.content.Context
import android.content.Intent
import com.shwifty.tex.confluence.ConfluenceDaemonService

/**
 * Created by arran on 26/05/2017.
 */
fun Context.startConfluenceDaemon(){
    val daemonIntent = Intent(this, ConfluenceDaemonService::class.java)
    daemonIntent.addCategory(ConfluenceDaemonService.TAG)
    this.startService(daemonIntent)
}