package com.shwifty.tex.views.showtorrent.mvi

import com.arranlomas.kontent.commons.objects.KontentContract

/**
 * Created by arran on 16/04/2017.
 */
interface TorrentInfoContract {
    interface ViewModel : KontentContract.ViewModel<TorrentInfoActions, TorrentInfoResult, TorrentInfoViewState>
}