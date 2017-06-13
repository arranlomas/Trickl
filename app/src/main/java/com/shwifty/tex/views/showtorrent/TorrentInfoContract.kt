package com.shwifty.tex.views.showtorrent

import android.content.Context
import android.os.Bundle
import android.view.MenuItem

/**
 * Created by arran on 16/04/2017.
 */
interface TorrentInfoContract {
    interface View {
        fun notifyTorrentDeleted()
        fun showError(stringId: Int)
        fun showInfo(stringId: Int)
        fun showSuccess(stringId: Int)
        fun notifyTorrentAdded()
        fun dismiss()
    }

    interface Presenter {
        fun setup(context: Context, view: View, arguments: Bundle?)
        fun destroy()
        var torrentHash: String
        var torrentMagnet: String?
        var torrentName: String?
        var torrentTrackers: List<String>?
        fun fetchTorrent()
        fun optionsItemSelected(item: MenuItem)
    }
}