package com.schiwfty.tex.splash

import android.content.Context
import android.content.Intent
import com.schiwfty.tex.confluence.Confluence
import com.schiwfty.tex.confluence.ConfluenceDaemonService

/**
 * Created by arran on 16/04/2017.
 */
class SplashPresenter : SplashContract.Presenter {
    lateinit var view: SplashContract.View

    override fun setClienctAddress() {
        Confluence.setClientAddress()
    }

    override fun setup(view: SplashContract.View) {
        this.view = view
    }

    override fun startConfluenceDaemon(context: Context) {
        val daemonIntent = Intent(context, ConfluenceDaemonService::class.java)
        daemonIntent.addCategory(ConfluenceDaemonService.TAG)
        context.startService(daemonIntent)
    }
}