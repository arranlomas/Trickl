package com.schiwfty.tex.views.torrentdetails.mvp

import android.content.Context
import android.os.Bundle
import com.schiwfty.tex.models.TorrentInfo

/**
 * Created by arran on 7/05/2017.
 */
interface TorrentDetailsContract {
    interface View {
        fun setupViewFromTorrentInfo(torrentInfo: TorrentInfo)

    }

    interface Presenter {
        fun setup(context: Context, view: TorrentDetailsContract.View, arguments: Bundle?)
        fun getTorrent(torrentHash: String)
        var torrentHash: String
    }
}