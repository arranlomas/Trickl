package com.shwifty.tex.views.search.mvi

import com.arranlomas.kontent.commons.objects.KontentContract

/**
 * Created by arran on 7/05/2017.
 */
interface TorrentSearchContract {
    interface ViewModel : KontentContract.ViewModel<SearchActions, SearchResult, SearchViewState>
}