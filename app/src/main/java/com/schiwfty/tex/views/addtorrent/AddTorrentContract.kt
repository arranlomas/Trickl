package com.schiwfty.tex.views.addtorrent

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
        var torrentHash: String
        fun fetchTorrent(hash: String)
        fun notifyAddTorrentClicked(hash: String)
    }
}