package com.shwifty.tex.views.browse.mvp

import com.shwifty.tex.models.TorrentSearchResult
import com.shwifty.tex.views.base.BaseContract

/**
 * Created by arran on 7/05/2017.
 */
interface TorrentBrowseContract {
    interface View : BaseContract.MvpView {
        fun showTorrents(searchResults: List<TorrentSearchResult>)
    }

    interface Presenter : BaseContract.Presenter<View> {

    }
}