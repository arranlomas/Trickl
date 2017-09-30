package com.shwifty.tex.views.all.mvp

import android.content.Context
import com.schiwfty.torrentwrapper.models.TorrentInfo
import com.shwifty.tex.views.base.BaseContract

/**
 * Created by arran on 16/04/2017.
 */
interface AllContract {
    interface View: BaseContract.MvpView {
        fun showAllTorrents(torrentInfoList: List<TorrentInfo>)
    }

    interface Presenter: BaseContract.Presenter<View> {
        fun refresh()
    }
}