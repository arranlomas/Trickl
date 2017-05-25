package com.schiwfty.tex.views.main.mvp

import android.content.Context

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
    }

    interface Presenter {
        fun setup(context: Context, view: MainContract.View)
        fun showAddTorrentActivity(hash: String? = null, magnet: String? = null, torrentFilePath: String? = null)
        fun showTorrentInfoActivity(infoHash: String)
    }
}