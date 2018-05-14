package com.shwifty.tex.views.search.mvi

import com.shwifty.tex.repository.network.torrentSearch.ITorrentSearchRepository
import com.shwifty.tex.repository.searchhistory.ISearchHistoryRepository
import com.shwifty.tex.views.base.mvi.BaseMviViewModel
import javax.inject.Inject

/**
 * Created by arran on 7/05/2017.
 */
class TorrentSearchViewModel @Inject constructor(
    torrentSearchRepository: ITorrentSearchRepository,
    searchHistoryRepository: ISearchHistoryRepository
)
    : TorrentSearchContract.ViewModel, BaseMviViewModel<SearchActions, SearchResult, SearchViewState>(
    actionProcessor = searchActionProcessor(torrentSearchRepository, searchHistoryRepository),
    reducer = searchReducer,
    defaultState = SearchViewState.default()
)