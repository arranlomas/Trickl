package com.shwifty.tex.views.main.mvp

import android.content.Context
import android.content.Intent
import com.shwifty.tex.MyApplication
import com.shwifty.tex.R
import com.shwifty.tex.TricklComponent
import com.shwifty.tex.models.TorrentFile
import com.shwifty.tex.repositories.ITorrentRepository
import com.shwifty.tex.utils.CONNECTIVITY_STATUS
import com.shwifty.tex.utils.canCast
import com.shwifty.tex.utils.getConnectivityStatus
import com.shwifty.tex.views.splash.mvp.SplashActivity
import javax.inject.Inject

/**
 * Created by arran on 16/04/2017.
 */
class MainPresenter : MainContract.Presenter {

    lateinit var view: MainContract.View

    lateinit var context: Context

    @Inject
    lateinit var torrentRepository: ITorrentRepository

    override fun setup(context: Context, view: MainContract.View) {
        this.view = view
        this.context = context
        TricklComponent.mainComponent.inject(this)
    }

    override fun showAddTorrentActivity(hash: String?, magnet: String?, torrentFilePath: String?) {
        view.showAddTorrentActivity(hash, magnet, torrentFilePath)
    }

    override fun showTorrentInfoActivity(infoHash: String) {
        view.showTorrentInfoActivity(infoHash)
    }

    override fun handleIntent(intent: Intent) {
        if (intent.hasExtra(SplashActivity.TAG_MAGNET_FROM_INTENT)) {
            view.showAddTorrentActivity(magnet = intent.getStringExtra(SplashActivity.TAG_MAGNET_FROM_INTENT))
        }
    }

    override fun checkStatusForDownload(torrentFile: TorrentFile) {
        val status = context.getConnectivityStatus()
        when (status) {
            CONNECTIVITY_STATUS.WIFI -> torrentRepository.startFileDownloading(torrentFile, context, true)
            CONNECTIVITY_STATUS.MOBILE -> view.showNoWifiDialog(torrentFile)
            CONNECTIVITY_STATUS.NOT_CONNECTED -> view.showError(R.string.error_not_connected_to_wifi)
        }
    }

    override fun startChromecast(torrentFile: TorrentFile) {
        if (torrentFile.canCast()) {
            val casted = MyApplication.castHandler.loadRemoteMedia(torrentFile)
            if (!casted) view.showError(R.string.chromecast_not_connect)
        } else {
            view.showError(R.string.error_file_not_supported_by_chromecast)
        }
    }
}