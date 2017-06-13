package com.shwifty.tex.views.torrentdetails.mvp

import android.content.Context
import android.os.Bundle
import com.shwifty.tex.models.TorrentInfo

/**
 * Created by arran on 7/05/2017.
 */
interface TorrentDetailsContract {
    interface View {
        fun setupViewFromTorrentInfo(torrentInfo: TorrentInfo)

    }

    interface Presenter {
        fun setup(context: Context, view: View, arguments: Bundle?)
        fun loadTorrent(torrentHash: String)
        var torrentHash: String
    }
}