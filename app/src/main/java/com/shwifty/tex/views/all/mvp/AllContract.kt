package com.shwifty.tex.views.all.mvp

import com.schiwfty.torrentwrapper.models.TorrentInfo
import com.shwifty.tex.views.base.mvp.BaseContract

/**
 * Created by arran on 16/04/2017.
 */
interface AllContract {
    interface View: BaseContract.MvpView {
        fun showAllTorrents(torrentInfoList: List<TorrentInfo>)
        fun showSomeTorrentsCouldNotBeLoaded(torrentCount: Int)
    }

    interface Presenter: BaseContract.Presenter<View> {
        fun refresh()
    }
}