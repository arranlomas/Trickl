package com.shwifty.tex.views.splash.mvp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.schiwfty.torrentwrapper.confluence.Confluence
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.schiwfty.torrentwrapper.utils.startConfluenceDaemon
import com.shwifty.tex.R
import rx.subscriptions.CompositeSubscription

/**
 * Created by arran on 16/04/2017.
 */
class SplashPresenter : SplashContract.Presenter {

    override var magnet: String? = null

    override fun handleIntent(intent: Intent) {
        val magnetCandidate = intent.dataString ?: return
        if(magnetCandidate.startsWith("magnet")){
            magnet = magnetCandidate
        }
    }

    lateinit var view: SplashContract.View

    lateinit var torrentRepository: ITorrentRepository

    var subscriptions = CompositeSubscription()

    override fun setup(context: Context, view: SplashContract.View) {
        this.view = view
        torrentRepository = Confluence.torrentRepository
    }

    override fun destroy() {
        subscriptions.unsubscribe()
    }

    override fun startConfluenceDaemon(context: Context) {
        Confluence.torrentRepository.isConnected()
                .subscribe({ started ->
                    if (!started) Confluence.start(context as Activity, R.drawable.trickl_notification, false, {
                        Log.v("Error", "Storage permissions is required to start the client")
                    })
                }, {
                    Log.v("client already running", it.localizedMessage)
                })
        listenForDaemon()
    }


    private fun listenForDaemon() {
        subscriptions.add(torrentRepository.getStatus()
                .retry()
                .subscribe({
                    subscriptions.unsubscribe()
                    view.showSuccess(R.string.splash_start_confluence_success)
                    view.progressToMain()
                }, {
                    view.showError(R.string.splash_start_confluence_error)
                }))

    }

}