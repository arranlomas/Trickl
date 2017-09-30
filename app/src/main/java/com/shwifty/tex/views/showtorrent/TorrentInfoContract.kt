package com.shwifty.tex.views.showtorrent

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import com.schiwfty.torrentwrapper.models.TorrentInfo
import com.shwifty.tex.views.base.BaseContract

/**
 * Created by arran on 16/04/2017.
 */
interface TorrentInfoContract {
    interface View: BaseContract.MvpView {
        fun notifyTorrentDeleted()
        fun notifyTorrentAdded()
        fun dismiss()
    }

    interface Presenter: BaseContract.Presenter<View> {
        fun setup(arguments: Bundle?)
        var torrentInfo: TorrentInfo?
        var torrentHash: String
        var torrentMagnet: String?
        var torrentName: String?
        var torrentTrackers: List<String>?
        fun fetchTorrent()
        fun optionsItemSelected(item: MenuItem)
    }
}