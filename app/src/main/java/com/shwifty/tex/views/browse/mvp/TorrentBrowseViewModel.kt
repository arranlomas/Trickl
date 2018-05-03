package com.shwifty.tex.views.browse.mvp

import com.shwifty.tex.repository.network.torrentSearch.ITorrentSearchRepository
import com.shwifty.tex.views.base.mvi.BaseMviViewModel
import com.shwifty.tex.views.search.mvi.SearchActions
import com.shwifty.tex.views.search.mvi.SearchResult
import com.shwifty.tex.views.search.mvi.SearchViewState
import com.shwifty.tex.views.search.mvi.TorrentSearchContract
import javax.inject.Inject

/**
 * Created by arran on 7/05/2017.
 */
class TorrentBrowseViewModel @Inject constructor(torrentSearchRepository: ITorrentSearchRepository)
    : TorrentSearchContract.ViewModel, BaseMviViewModel<SearchActions, SearchResult, SearchViewState>(
    actionProcessor = browseActionProcessor(torrentSearchRepository),
    reducer = browseReducer,
    defaultState = SearchViewState.default()
)