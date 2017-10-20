package com.shwifty.tex.views.splash.mvp

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.schiwfty.torrentwrapper.confluence.Confluence
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.R
import com.shwifty.tex.views.base.BasePresenter

/**
 * Created by arran on 16/04/2017.
 */
class SplashPresenter : BasePresenter<SplashContract.View>(), SplashContract.Presenter {

    override var magnet: String? = null

    override fun handleIntent(intent: Intent) {
        val magnetCandidate = intent.dataString ?: return
        if (magnetCandidate.startsWith("magnet")) {
            magnet = magnetCandidate
        }
    }

    lateinit var torrentRepository: ITorrentRepository

    override fun attachView(mvpView: SplashContract.View) {
        super.attachView(mvpView)
        torrentRepository = Confluence.torrentRepository
    }

    override fun startConfluenceDaemon(context: Context) {
        val notificationIntent = Intent(context, SplashActivity::class.java)
        val seed = false
        val stopActionInNotification = true
        Confluence.torrentRepository.isConnected()
                .subscribe(object : BaseSubscriber<Boolean>() {
                    override fun onNext(started: Boolean) {
                        mvpView.setLoading(false)
                        if (!started) Confluence.start(context as Activity, R.drawable.trickl_notification, seed, stopActionInNotification, notificationIntent, {
                            mvpView.showError("Storage permissions is required to start the client")
                        })
                    }
                })
                .addSubscription()
        listenForDaemon()
    }

    private fun listenForDaemon() {
        torrentRepository.getStatus()
                .retry()
                .subscribe(object : BaseSubscriber<String>() {
                    override fun onNext(pair: String?) {
                        mvpView.setLoading(false)
                        pair?.let {
                            subscriptions.unsubscribe()
                            mvpView.showSuccess(R.string.splash_start_confluence_success)
                            mvpView.progressToMain()
                        }
                    }
                })
                .addSubscription()
    }
}