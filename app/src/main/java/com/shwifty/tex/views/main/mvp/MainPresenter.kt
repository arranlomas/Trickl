package com.shwifty.tex.views.main.mvp

import android.content.Intent
import com.schiwfty.torrentwrapper.models.TorrentFile
import com.schiwfty.torrentwrapper.models.TorrentInfo
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.schiwfty.torrentwrapper.utils.canCast
import com.shwifty.tex.MyApplication
import com.shwifty.tex.R
import com.shwifty.tex.chromecast.CastHandler
import com.shwifty.tex.utils.CONNECTIVITY_STATUS
import com.shwifty.tex.views.base.BasePresenter
import com.shwifty.tex.views.main.MainEventHandler
import com.shwifty.tex.views.splash.mvp.SplashActivity

/**
 * Created by arran on 16/04/2017.
 */
class MainPresenter(val torrentRepository: ITorrentRepository) : BasePresenter<MainContract.View>(), MainContract.Presenter {

    override fun attachView(mvpView: MainContract.View) {
        super.attachView(mvpView)
        setupEvents()
        MyApplication.castHandler.stateListener
                .subscribe(object : BaseSubscriber<CastHandler.PlayerState>() {
                    override fun onNext(stat: CastHandler.PlayerState?) {
                        when (stat) {
                            CastHandler.PlayerState.DISCONNECTED, CastHandler.PlayerState.OTHER, null -> mvpView.showChromecastController(false)
                            else -> mvpView.showChromecastController(true)
                        }
                    }
                })
    }

    override fun showAddTorrentActivity(hash: String?, magnet: String?, torrentFilePath: String?) {
        mvpView.showAddTorrentActivity(hash, magnet, torrentFilePath)
    }

    override fun showTorrentInfoActivity(infoHash: String) {
        mvpView.showTorrentInfoActivity(infoHash)
    }

    override fun handleIntent(intent: Intent) {
        if (intent.hasExtra(SplashActivity.TAG_MAGNET_FROM_INTENT)) {
            mvpView.showAddTorrentActivity(magnet = intent.getStringExtra(SplashActivity.TAG_MAGNET_FROM_INTENT))
        }
    }

    override fun checkStatusForDownload(torrentFile: TorrentFile) {
        when (mvpView.getConnectivityStatus()) {
            CONNECTIVITY_STATUS.WIFI -> mvpView.startFileDownloading(torrentFile, torrentRepository)
            CONNECTIVITY_STATUS.MOBILE -> mvpView.showNoWifiDialog(torrentFile)
            CONNECTIVITY_STATUS.NOT_CONNECTED -> mvpView.showError(R.string.error_not_connected_to_wifi)
        }
    }

    override fun startChromecast(torrentFile: TorrentFile) {
        if (torrentFile.canCast()) {
            val casted = MyApplication.castHandler.loadRemoteMedia(torrentFile)
            if (!casted) mvpView.showError(R.string.chromecast_not_connect)
        } else {
            mvpView.showError(R.string.error_file_not_supported_by_chromecast)
        }
    }

    private fun setupEvents() {
        MainEventHandler.addTorrentPublishSubject
                .subscribe(object : BaseSubscriber<Pair<MainEventHandler.AddTorrentType, String>>() {
                    override fun onNext(pair: Pair<MainEventHandler.AddTorrentType, String>) {
                        val (type, magnetOrHash) = pair
                        when (type) {
                            MainEventHandler.AddTorrentType.MAGNET -> showAddTorrentActivity(magnet = magnetOrHash)
                            MainEventHandler.AddTorrentType.HASH -> showAddTorrentActivity(hash = magnetOrHash)
                        }
                    }
                })
                .addSubscription()

        MainEventHandler.showTorrentInfoPublishSubject
                .subscribe(object : BaseSubscriber<TorrentInfo>() {
                    override fun onNext(torrentInfo: TorrentInfo) {
                        mvpView.showTorrentInfoActivity(torrentInfo.info_hash)
                    }
                })
                .addSubscription()

        MainEventHandler.eventPublishSubject
                .subscribe(object : BaseSubscriber<Pair<MainEventHandler.Action, TorrentFile>>() {
                    override fun onNext(pair: Pair<MainEventHandler.Action, TorrentFile>) {
                        val (event, torrentFile) = pair
                        when (event) {
                            MainEventHandler.Action.DOWNLOAD -> checkStatusForDownload(torrentFile)
                            MainEventHandler.Action.CHROMECAST -> startChromecast(torrentFile)
                            MainEventHandler.Action.OPEN -> mvpView.openTorrentFile(torrentFile, torrentRepository)
                            MainEventHandler.Action.DELETE -> mvpView.openDeleteTorrentDialog(torrentFile)
                        }
                    }
                })
                .addSubscription()
    }
}