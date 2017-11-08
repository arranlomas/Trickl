package com.shwifty.tex.views.browse.mvp

import com.shwifty.tex.models.TorrentSearchCategory
import com.shwifty.tex.models.TorrentSearchSortType
import com.shwifty.tex.views.base.mvp.BaseContract
import com.shwifty.tex.views.browse.state.BrowseReducer

/**
 * Created by arran on 7/05/2017.
 */
interface TorrentBrowseContract {
    interface View : BaseContract.MvpView {
        val browseReducer: BrowseReducer
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun load(sortType: TorrentSearchSortType, category: TorrentSearchCategory)
        fun search(query: String)
    }
}