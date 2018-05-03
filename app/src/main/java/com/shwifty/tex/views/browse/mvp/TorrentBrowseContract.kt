package com.shwifty.tex.views.browse.mvp

import com.arranlomas.kontent.commons.objects.KontentContract

/**
 * Created by arran on 7/05/2017.
 */
interface TorrentBrowseContract {
    interface ViewModel : KontentContract.ViewModel<BrowseActions, BrowseResult, BrowseViewState>
}