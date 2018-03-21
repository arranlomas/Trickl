package com.shwifty.tex.views.showtorrent.mvi

import com.arranlomas.kontent.commons.objects.KontentResult
import com.schiwfty.torrentwrapper.models.TorrentInfo
import com.shwifty.tex.models.TorrentSearchCategory
import com.shwifty.tex.models.TorrentSearchResult
import com.shwifty.tex.models.TorrentSearchSortType

/**
 * Created by arran on 14/02/2018.
 */

sealed class TorrentInfoResult : KontentResult() {
    data class LoadSuccess(val torrentInfo: TorrentInfo) : TorrentInfoResult()
    data class LoadError(val error: Throwable) : TorrentInfoResult()
    class LoadInFlight : TorrentInfoResult()
}