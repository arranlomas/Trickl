package com.shwifty.tex.views.showtorrent.mvp

import com.shwifty.tex.views.base.mvp.BaseContract

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
        fun setup(arguments: android.os.Bundle?)
        var torrentInfo: com.schiwfty.torrentwrapper.models.TorrentInfo?
        var torrentHash: String
        var torrentMagnet: String?
        var torrentName: String?
        var torrentTrackers: List<String>?
        fun fetchTorrent()
        fun optionsItemSelected(item: android.view.MenuItem)
    }
}