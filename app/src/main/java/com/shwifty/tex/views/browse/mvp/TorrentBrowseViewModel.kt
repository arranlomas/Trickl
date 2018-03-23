package com.shwifty.tex.views.browse.mvp

import com.shwifty.tex.repository.network.torrentSearch.ITorrentSearchRepository
import com.shwifty.tex.views.base.mvi.BaseMviViewModel
import javax.inject.Inject

/**
 * Created by arran on 7/05/2017.
 */
class TorrentBrowseViewModel @Inject constructor(torrentSearchRepository: ITorrentSearchRepository)
    : TorrentBrowseContract.ViewModel, BaseMviViewModel<BrowseActions, BrowseResult, BrowseViewState>(
        actionProcessor = browseActionProcessor(torrentSearchRepository),
        reducer = browseReducer,
        defaultState = BrowseViewState.default()
)