package com.shwifty.tex.views.addtorrent.mvi

import com.arranlomas.kontent.commons.objects.KontentAction
import com.shwifty.tex.models.TorrentSearchCategory
import com.shwifty.tex.models.TorrentSearchSortType

/**
 * Created by arran on 14/02/2018.
 */
sealed class AddTorrentActions : KontentAction() {
    data class Load(val torrentHash: String, val trackers: List<String>?) : AddTorrentActions()
    data class RemoveTorrent(val torrentHash: String) : AddTorrentActions()

}
