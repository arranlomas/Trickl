package com.shwifty.tex.views.showtorrent.mvi

import com.arranlomas.kontent.commons.objects.KontentAction
import com.shwifty.tex.models.TorrentSearchCategory
import com.shwifty.tex.models.TorrentSearchSortType

/**
 * Created by arran on 14/02/2018.
 */
sealed class TorrentInfoActions : KontentAction() {
    data class Load(val torrentHash: String) : TorrentInfoActions()
}
