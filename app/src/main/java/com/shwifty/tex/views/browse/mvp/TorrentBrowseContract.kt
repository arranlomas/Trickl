package com.shwifty.tex.views.browse.mvp

import com.arranlomas.kontent.commons.objects.KontentContract
import com.shwifty.tex.views.search.mvi.SearchViewState
import com.shwifty.tex.views.search.mvi.SearchResult
import com.shwifty.tex.views.search.mvi.SearchActions

/**
 * Created by arran on 7/05/2017.
 */
interface TorrentBrowseContract {
    interface ViewModel : KontentContract.ViewModel<SearchActions, SearchResult, SearchViewState>
}