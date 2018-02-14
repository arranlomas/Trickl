package com.shwifty.tex.views.browse.mvp

import com.shwifty.tex.repository.network.torrentSearch.ITorrentSearchRepository
import com.shwifty.tex.views.base.mvi.BaseMviInteractor

/**
 * Created by arran on 7/05/2017.
 */
class TorrentBrowseInteractor(torrentSearchRepository: ITorrentSearchRepository)
    : TorrentBrowseContract.Interactor, BaseMviInteractor<BrowseIntents, BrowseActions, BrowseResult, BrowseViewState>(
        intentToAction = { browseIntentToAction(it) },
        actionProcessor = browseActionProcessor(torrentSearchRepository),
        reducer = browseResucer,
        defaultState = BrowseViewState.default()
)