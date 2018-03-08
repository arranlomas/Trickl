package com.shwifty.tex.views.addtorrent.mvi

import com.arranlomas.kontent.commons.objects.KontentIntent

/**
 * Created by arran on 11/11/2017.
 */
sealed class AddTorrentIntent : KontentIntent() {
    data class LoadIntent(val torrentHash: String) : AddTorrentIntent()
}