package com.shwifty.tex.views.main.mvp

import android.content.Context
import android.content.Intent
import com.schiwfty.torrentwrapper.models.TorrentFile

/**
 * Created by arran on 16/04/2017.
 */
interface MainContract {
    interface View {
        fun showError(stringId: Int)
        fun showInfo(stringId: Int)
        fun showSuccess(stringId: Int)
        fun showTorrentInfoActivity(infoHash: String)
        fun showAddTorrentActivity(hash: String? = null, magnet: String? = null, torrentFilePath: String? = null)
        fun showNoWifiDialog(torrentFile: TorrentFile)
    }

    interface Presenter {
        fun setup(context: Context, view: MainContract.View)
        fun showAddTorrentActivity(hash: String? = null, magnet: String? = null, torrentFilePath: String? = null)
        fun showTorrentInfoActivity(infoHash: String)
        fun handleIntent(intent: Intent)
        fun checkStatusForDownload(torrentFile: TorrentFile)
        fun startChromecast(torrentFile: TorrentFile)
    }
}