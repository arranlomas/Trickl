package com.shwifty.tex.views.addtorrent.mvp

import android.os.Bundle
import com.shwifty.tex.views.base.BaseContract

/**
 * Created by arran on 16/04/2017.
 */
interface AddTorrentContract {
    interface View : com.shwifty.tex.views.base.BaseContract.MvpView {
        fun notifyTorrentAdded()
    }

    interface Presenter : com.shwifty.tex.views.base.BaseContract.Presenter<View> {
        fun setup(arguments: android.os.Bundle?)
        var torrentHash: String?
        var torrentMagnet: String?
        var torrentName: String?
        var torrentTrackers: List<String>?
        fun fetchTorrent()
    }
}