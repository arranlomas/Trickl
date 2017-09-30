package com.shwifty.tex.views.torrentdetails.mvp

import android.content.Context
import android.os.Bundle
import com.schiwfty.torrentwrapper.models.TorrentInfo
import com.shwifty.tex.views.base.BaseContract

/**
 * Created by arran on 7/05/2017.
 */
interface TorrentDetailsContract {
    interface View: BaseContract.MvpView {
        fun setupViewFromTorrentInfo(torrentInfo: TorrentInfo)

    }

    interface Presenter: BaseContract.Presenter<View> {
        fun setup(arguments: Bundle?)
        fun loadTorrent(torrentHash: String)
        var torrentHash: String
    }
}