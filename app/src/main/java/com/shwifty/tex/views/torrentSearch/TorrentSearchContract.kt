package com.shwifty.tex.views.torrentSearch

import com.shwifty.tex.models.TorrentSearchResult
import com.shwifty.tex.views.base.BaseContract

/**
 * Created by arran on 7/05/2017.
 */
interface TorrentSearchContract {
    interface View : BaseContract.MvpView {
        fun showTorrents(searchResults: List<TorrentSearchResult>)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun search(query: String)
    }
}