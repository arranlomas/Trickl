package com.shwifty.tex.views.showtorrent.mvi

import com.arranlomas.kontent.commons.objects.KontentContract
import com.shwifty.tex.views.showtorrent.mvi.TorrentInfoActions
import com.shwifty.tex.views.showtorrent.mvi.TorrentInfoIntent
import com.shwifty.tex.views.showtorrent.mvi.TorrentInfoResult
import com.shwifty.tex.views.showtorrent.mvi.TorrentInfoViewState

/**
 * Created by arran on 16/04/2017.
 */
interface TorrentInfoContract {
    interface ViewModel : KontentContract.ViewModel<TorrentInfoIntent, TorrentInfoActions, TorrentInfoResult, TorrentInfoViewState>
}