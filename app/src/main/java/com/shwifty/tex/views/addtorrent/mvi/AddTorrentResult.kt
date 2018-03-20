package com.shwifty.tex.views.addtorrent.mvi

import com.arranlomas.kontent.commons.objects.KontentResult
import com.schiwfty.torrentwrapper.models.TorrentInfo
import com.shwifty.tex.models.TorrentSearchCategory
import com.shwifty.tex.models.TorrentSearchResult
import com.shwifty.tex.models.TorrentSearchSortType

/**
 * Created by arran on 14/02/2018.
 */

sealed class AddTorrentResult : KontentResult() {
    data class LoadSuccess(val torrentInfo: TorrentInfo, val torrentAlreadyExisted: Boolean) : AddTorrentResult()
    data class LoadError(val error: Throwable) : AddTorrentResult()
    class LoadInFlight : AddTorrentResult()
    class RemoveSuccess : AddTorrentResult()
    data class RemoveError(val error: Throwable) : AddTorrentResult()
    class RemoveInFlight : AddTorrentResult()
}