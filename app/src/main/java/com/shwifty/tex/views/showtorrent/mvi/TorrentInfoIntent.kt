package com.shwifty.tex.views.showtorrent.mvi

import com.arranlomas.kontent.commons.objects.KontentIntent

/**
 * Created by arran on 11/11/2017.
 */
sealed class TorrentInfoIntent : KontentIntent() {
    data class LoadInfoIntent(val torrentHash: String) : TorrentInfoIntent()
}