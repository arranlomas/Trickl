package com.shwifty.tex.views.addtorrent

import android.content.Context
import android.os.Bundle

/**
 * Created by arran on 16/04/2017.
 */
interface AddTorrentContract {
    interface View {
        fun showError(stringId: Int)
        fun showInfo(stringId: Int)
        fun showSuccess(stringId: Int)
        fun notifyTorrentAdded()
    }

    interface Presenter {
        fun setup(context: Context, view: View, arguments: Bundle?)
        var torrentHash: String?
        var torrentMagnet: String?
        var torrentName: String?
        var torrentTrackers: List<String>?
        fun fetchTorrent()
    }
}