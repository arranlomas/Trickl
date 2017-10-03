package com.shwifty.tex.views.showtorrent.mvp

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import com.schiwfty.torrentwrapper.models.TorrentInfo
import com.shwifty.tex.views.base.BaseContract

/**
 * Created by arran on 16/04/2017.
 */
interface TorrentInfoContract {
    interface View: com.shwifty.tex.views.base.BaseContract.MvpView {
        fun notifyTorrentDeleted()
        fun notifyTorrentAdded()
        fun dismiss()
    }

    interface Presenter: com.shwifty.tex.views.base.BaseContract.Presenter<View> {
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