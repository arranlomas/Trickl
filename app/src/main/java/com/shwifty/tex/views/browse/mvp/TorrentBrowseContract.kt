package com.shwifty.tex.views.browse.mvp

import com.arranlomas.kontent.commons.objects.mvi.KontentContract

/**
 * Created by arran on 7/05/2017.
 */
interface TorrentBrowseContract {
    interface Interactor : KontentContract.Interactor<BrowseIntents, BrowseViewState>
}