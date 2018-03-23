package com.shwifty.tex.views.addtorrent.mvi

import com.arranlomas.kontent.commons.objects.KontentContract

/**
 * Created by arran on 16/04/2017.
 */
interface AddTorrentContract {
    interface ViewModel : KontentContract.ViewModel<AddTorrentActions, AddTorrentResult, AddTorrentViewState>
}