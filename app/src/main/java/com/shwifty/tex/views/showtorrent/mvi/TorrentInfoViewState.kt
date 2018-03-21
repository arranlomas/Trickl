package com.shwifty.tex.views.showtorrent.mvi

import com.arranlomas.kontent.commons.objects.KontentViewState
import com.schiwfty.torrentwrapper.models.TorrentInfo

/**
 * Created by arran on 11/11/2017.
 */

data class TorrentInfoViewState(
        val isLoading: Boolean = false,
        val error: String? = null,
        val result: TorrentInfo? = null,
        val torrentHash: String? = null
) : KontentViewState() {
    companion object Factory {
        fun default(): TorrentInfoViewState {
            return TorrentInfoViewState()
        }
    }
}