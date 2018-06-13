package com.shwifty.tex.views.splash.mvi

import com.arranlomas.kontent.commons.objects.KontentViewState
import com.schiwfty.torrentwrapper.models.TorrentInfo

/**
 * Created by arran on 11/11/2017.
 */

data class SplashViewState(
        val isLoading: Boolean = false,
        val error: String? = null,
        val torrentFile: String? = null,
        val magnet: String? = null,
        val permissionRequested: Boolean? = null,
        val permissionGranted: Boolean? = null
) : KontentViewState() {
    companion object Factory {
        fun default(): SplashViewState {
            return SplashViewState()
        }
    }
}