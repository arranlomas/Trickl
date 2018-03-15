package com.shwifty.tex.views.addtorrent.mvi

import com.arranlomas.kontent.commons.objects.KontentViewState
import com.schiwfty.torrentwrapper.models.TorrentInfo
import com.shwifty.tex.models.TorrentSearchCategory
import com.shwifty.tex.models.TorrentSearchResult
import com.shwifty.tex.models.TorrentSearchSortType

/**
 * Created by arran on 11/11/2017.
 */

data class AddTorrentViewState(
        val isLoading: Boolean = false,
        val error: String? = null,
        val result: TorrentInfo? = null,
        val torrentAlreadyExisted: Boolean = false,
        val torrentHash: String? = null,
        val torrentRemovedAndShouldRestart: Boolean = false
) : KontentViewState() {
    companion object Factory {
        fun default(): AddTorrentViewState {
            return AddTorrentViewState()
        }
    }
}