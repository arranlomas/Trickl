package com.shwifty.tex.views.main.mvp

import android.content.Intent
import com.schiwfty.torrentwrapper.confluence.Confluence
import com.schiwfty.torrentwrapper.models.TorrentFile
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.schiwfty.torrentwrapper.utils.canCast
import com.shwifty.tex.MyApplication
import com.shwifty.tex.R
import com.shwifty.tex.utils.CONNECTIVITY_STATUS
import com.shwifty.tex.views.base.BasePresenter
import com.shwifty.tex.views.splash.mvp.SplashActivity

/**
 * Created by arran on 16/04/2017.
 */
class MainPresenter : BasePresenter<MainContract.View>(), MainContract.Presenter {

    lateinit var torrentRepository: ITorrentRepository

    override fun attachView(mvpView: MainContract.View) {
        super.attachView(mvpView)
        torrentRepository = Confluence.torrentRepository
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
}