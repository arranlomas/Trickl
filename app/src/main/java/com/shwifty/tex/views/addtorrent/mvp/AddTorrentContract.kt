package com.shwifty.tex.views.addtorrent.mvp

import android.os.Bundle
import com.shwifty.tex.views.base.mvp.BaseContract

/**
 * Created by arran on 16/04/2017.
 */
interface AddTorrentContract {
    interface View : BaseContract.MvpView {
        fun notifyTorrentAdded()
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun setup(arguments: Bundle?)
        fun notifyBackPressed()
        var torrentHash: String?
        var torrentMagnet: String?
        var torrentName: String?
        var torrentTrackers: List<String>?
    }
}