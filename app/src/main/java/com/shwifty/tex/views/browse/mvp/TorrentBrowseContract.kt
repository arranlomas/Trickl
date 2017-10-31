package com.shwifty.tex.views.browse.mvp

import com.shwifty.tex.models.TorrentSearchCategory
import com.shwifty.tex.models.TorrentSearchResult
import com.shwifty.tex.models.TorrentSearchSortType
import com.shwifty.tex.views.base.BaseContract
import com.shwifty.tex.views.browse.state.Reducer

/**
 * Created by arran on 7/05/2017.
 */
interface TorrentBrowseContract {
    interface View : BaseContract.MvpView {
        val reducer: Reducer
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun load(sortType: TorrentSearchSortType, category: TorrentSearchCategory)
        fun search(query: String)
    }
}