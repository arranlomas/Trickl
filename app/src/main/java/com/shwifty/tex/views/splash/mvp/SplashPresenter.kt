package com.shwifty.tex.views.splash.mvp

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Build
import com.schiwfty.torrentwrapper.confluence.Confluence
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.R
import com.shwifty.tex.utils.getRealFilePath
import com.shwifty.tex.views.base.mvp.BasePresenter

/**
 * Created by arran on 16/04/2017.
 */
class SplashPresenter : BasePresenter<SplashContract.View>(), SplashContract.Presenter {

    override var magnet: String? = null
    override var torrentFile: String? = null

    override fun handleIntent(intent: Intent, contentResolver: ContentResolver) {
        val dataString = intent.dataString ?: return
        if (dataString.startsWith("magnet")) {
            magnet = dataString
        } else if (dataString.startsWith("content://")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Uri.parse(dataString).getRealFilePath(contentResolver)?.let {
                    torrentFile = it
                }
            }
        }
    }

    lateinit var torrentRepository: ITorrentRepository

    override fun attachView(mvpView: SplashContract.View) {
        super.attachView(mvpView)
        torrentRepository = Confluence.torrentRepository
    }

    override fun startConfluenceDaemon(activity: Activity) {
        val notificationIntent = Intent(activity, SplashActivity::class.java)
        val seed = false
        val stopActionInNotification = true
        val notificationChannelId = "trickl_daemon_notif"
        val notificationChannelName = "Trick Client Daemon"
        Confluence.torrentRepository.isConnected()
                .subscribeWith(object : BaseObserver<Boolean>() {
                    override fun onNext(started: Boolean) {
                        mvpView.setLoading(false)
                        if (!started) Confluence.start(activity, R.drawable.trickl_notification, notificationChannelId, notificationChannelName, seed, stopActionInNotification, notificationIntent, {
                            mvpView.showError("Storage permissions is required to start the client")
                        })
                    }
                })
                .addObserver()
        listenForDaemon()
    }

    private fun listenForDaemon() {
        torrentRepository.getStatus()
                .retry()
                .subscribeWith(object : BaseObserver<String>() {
                    override fun onNext(result: String) {
                        mvpView.setLoading(false)
                        subscriptions.unsubscribe()
                        disposables.dispose()
                        mvpView.showSuccess(R.string.splash_start_confluence_success)
                        mvpView.progressToMain()
                    }
                })
                .addObserver()
    }
}